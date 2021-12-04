package com.tsuenhsueh.tsuenmoverio;

import static android.Manifest.permission_group.CAMERA;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import com.epson.moverio.hardware.audio.AudioManager;
import com.epson.moverio.hardware.camera.CameraDevice;
import com.epson.moverio.hardware.camera.CameraManager;
import com.epson.moverio.hardware.camera.CameraProperty;
import com.epson.moverio.hardware.camera.CaptureDataCallback;
import com.epson.moverio.hardware.camera.CaptureStateCallback;
import com.epson.moverio.hardware.display.DisplayManager;
import com.epson.moverio.hardware.sensor.SensorDataListener;
import com.epson.moverio.hardware.sensor.SensorManager;

import java.io.Console;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity{

    private AudioManager audioManager;
    private DisplayManager displayManager;
    private SensorManager sensorManager;
    private SensorDataListener listener = data -> Log.d("iii", Arrays.toString(data.values) + "");
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CaptureDataCallback captureDataCallback = (l, bytes) -> {

    };
    private CaptureStateCallback captureStateCallback = new CaptureStateCallback() {
        @Override
        public void onCaptureStarted() {

        }

        @Override
        public void onCaptureStopped() {

        }

        @Override
        public void onPreviewStarted() {

        }

        @Override
        public void onPreviewStopped() {

        }

        @Override
        public void onRecordStarted() {

        }

        @Override
        public void onRecordStopped() {

        }

        @Override
        public void onPictureCompleted() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSensor();
        initCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initDisplay();
        initAudio();
    }

    private void initSensor() {
        sensorManager = new SensorManager(this);
        try {
            sensorManager.open(SensorManager.TYPE_GYROSCOPE, listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCamera() {
        SurfaceView surfaceView = findViewById(R.id.camera_surface);

        cameraManager = new CameraManager(this);
        try {
            cameraManager.open(captureStateCallback, captureDataCallback, surfaceView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }

        CameraProperty property = cameraDevice.getProperty();
        property.setCaptureSize(1280, 720);
        property.setCaptureFps(60);
        cameraDevice.setProperty(property);
        cameraDevice.startCapture();
        cameraDevice.startPreview();

    }

    private void initDisplay() {
        displayManager = new DisplayManager(this);
        try {
            displayManager.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // display manager is ready!

        displayManager.getBrightness();
        displayManager.setDisplayMode(DisplayManager.DISPLAY_MODE_3D);
    }

    private void initAudio() {
        audioManager = new AudioManager(this);
        try {
            audioManager.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // audio manager is ready!
        // audio manager is not commonly used, using the volume button on mobile phone is more convenient
    }

    @Override
    protected void onPause() {
        super.onPause();
        displayManager.close();
        audioManager.close();
        sensorManager.close(listener);
        cameraDevice.stopCapture();
        cameraDevice.stopPreview();
        cameraManager.close(cameraDevice);
    }

}