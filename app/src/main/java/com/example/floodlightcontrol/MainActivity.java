package com.example.floodlightcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("device1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // declare switch compat button
        SwitchCompat btn_beban1 = findViewById(R.id.beban1_btn);
        SwitchCompat btn_beban2 = findViewById(R.id.beban2_btn);
        SwitchCompat btn_beban3 = findViewById(R.id.beban3_btn);
        SwitchCompat btn_beban4 = findViewById(R.id.beban4_btn);


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // iterate over child
                int i = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.d("RTDB", "onDataChange: " + postSnapshot);
                    if (i==0){
                        Log.d("status", "onDataChange: " + String.valueOf(postSnapshot.child("status").getValue()) );
                        check_status(btn_beban1,String.valueOf(postSnapshot.child("status").getValue()));
                    } else if (i==1) {
                        check_status(btn_beban2,String.valueOf(postSnapshot.child("status").getValue()));
                    } else if (i==2) {
                        check_status(btn_beban3,String.valueOf(postSnapshot.child("status").getValue()));
                    } else if (i==3){
                        check_status(btn_beban4,String.valueOf(postSnapshot.child("status").getValue()));
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // btn listener
        controlTurnOnOff(btn_beban1,"light_1",dbRef, getApplicationContext());
        controlTurnOnOff(btn_beban2,"light_2",dbRef, getApplicationContext());
        controlTurnOnOff(btn_beban3,"light_3",dbRef, getApplicationContext());
        controlTurnOnOff(btn_beban4,"light_4",dbRef, getApplicationContext());

    }

    void controlTurnOnOff(SwitchCompat btn, String id_child, DatabaseReference updateData, Context applicationContext){
        btn.setShowText(true); // nampilin on or off
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // check status btn
                if (isChecked){
                    // set ke false
                    btn.getTextOn().toString();
                    btn.setTextOn("On");

                    // update to firebase with key command
                    updateData.child(id_child).child("command").setValue("ON"); // turn on

                    displayToast(applicationContext, "Turn ON");
                } else {
                    // set text to off
                    btn.getTextOff().toString();

                    // update to firebase with key command
                    updateData.child(id_child).child("command").setValue("OFF"); // turn off

                    displayToast(applicationContext, "Turn OFF");

                }

            }
        });

    }

    void check_status(SwitchCompat btn , String status){
        // compare status
        if(status.equals("ON")){
            // set button checked
            btn.setChecked(true);
        } else {
            // set button unchecked
            btn.setChecked(false);
        }
    }


    void displayToast(Context context, String message){

        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}