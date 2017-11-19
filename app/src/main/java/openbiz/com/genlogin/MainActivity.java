package openbiz.com.genlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    TextView tvUserCredential;
    private LoginButton fbLoginButton;
    private ProfilePictureView profilePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvUserCredential=findViewById(R.id.tvUserCredential);
        callbackManager = CallbackManager.Factory.create();
        profilePictureView = (ProfilePictureView) findViewById(R.id.user_pic);
        profilePictureView.setCropped(true);

        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                Toast.makeText(
                        MainActivity.this,
                        R.string.success,
                        Toast.LENGTH_LONG).show();
                updateUI();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(
                        MainActivity.this,
                        R.string.cancel,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(final FacebookException exception) {
                // App code
                Toast.makeText(
                        MainActivity.this,
                        R.string.error,
                        Toast.LENGTH_LONG).show();
            }
        });


        if(isLoggedIn())
        {
            updateUI();
        }
        new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    final Profile oldProfile,
                    final Profile currentProfile) {
                updateUI();
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isLoggedIn() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        return !(accesstoken == null || accesstoken.getPermissions().isEmpty());
    }

    private void updateUI() {
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            profilePictureView.setProfileId(profile.getId());
            tvUserCredential
                    .setText(String.format("%s %s",profile.getFirstName(), profile.getLastName()));
        } else {
            profilePictureView.setProfileId(null);
            tvUserCredential.setText(getString(R.string.welcome));
        }
    }
}
