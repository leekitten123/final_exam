package com.example.igx.problem1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URI;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Double latitude;
    Double longitude;

    SensorManager sensorManager ;

    Sensor gravitySensor ;
    Sensor accelerometerSensor ;
    Sensor linearSensor ;
    Sensor gyroscopeSensor ;

    float[][] values = new float[4][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_getLocation = (Button) findViewById(R.id.btn_getLocation);
        Button btn_getSensors = (Button) findViewById(R.id.btn_getSensors);
        Button btn_sendMessage = (Button) findViewById(R.id.btn_sendMessage);

        final TextView text_selectedData = (TextView) findViewById(R.id.text_selectedData);
        final TextView text_selectedType = (TextView) findViewById(R.id.text_selectedType);
        final EditText edit_phoneNumber = (EditText) findViewById(R.id.edit_phoneNumber);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        linearSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
                GPS gps = new GPS();

                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gps);
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gps);

                text_selectedType.setText("LOCATION");
                String locationString = "Latitude: " + latitude + "\nLongitude: " + longitude ;
                text_selectedData.setText(locationString);
            }
        });

        btn_getSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_selectedType.setText("SENSORS");

                text_selectedData.setText("");

                for (int i = 0 ; i < 4 ; i++) {
                    for (int j = 0 ; j < 3 ; j++) {
                        text_selectedData.append(values[i][j] + " ");
                    }

                    text_selectedData.append("\n");
                }
            }
        });

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, linearSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
        {
            for (int i = 0 ; i < 3 ; i++) {
                values[0][i] = event.values[i] ;
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            for (int i = 0 ; i < 3 ; i++) {
                values[1][i] = event.values[i] ;
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
        {
            for (int i = 0 ; i < 3 ; i++) {
                values[2][i] = event.values[i] ;
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            for (int i = 0 ; i < 3 ; i++) {
                values[3][i] = event.values[i] ;
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }

    class GPS implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
