package dam.isi.frsf.utn.edu.ar.laboratorio7y8;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class ReclamoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private boolean activarPermiso;
    private static final int PERMISO_DE_ACCESO = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reclamo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onMapReady(GoogleMap googleMap) {

        myMap=googleMap;

        // hay que determinar la versión primero, para poder pedir permiso de localización
        activarPermiso = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // si es mayor a API 6
            if (ContextCompat.checkSelfPermission(ReclamoActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ReclamoActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReclamoActivity.this);
                    builder.setTitle("Activar Localización.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("La aplicación requiere activar la localización de su dispositivo. ¿Autoriza activar el permiso?");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            activarPermiso = true;
                            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_DE_ACCESO);
                        }
                    });
                    builder.show();
                } else {
                    activarPermiso = true;
                    ActivityCompat.requestPermissions(ReclamoActivity.this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION} , PERMISO_DE_ACCESO);
                }
            }
        }
        if (!activarPermiso){

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }

            myMap.setMyLocationEnabled(true);

            }
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ReclamoActivity.PERMISO_DE_ACCESO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }

                    myMap.setMyLocationEnabled(true);

                } else {
                    finish();
                    Toast.makeText(ReclamoActivity.this, "No ha permitido el acceso a su localización.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
