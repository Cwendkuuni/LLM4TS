import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.auth.AccessToken;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {
    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("YOUR_CONSUMER_KEY")
                .setOAuthConsumerSecret("YOUR_CONSUMER_SECRET")
                .setOAuthAccessToken("YOUR_ACCESS_TOKEN")
                .setOAuthAccessTokenSecret("YOUR_ACCESS_TOKEN_SECRET");

        OAuthAuthorization auth = AuthorizationFactory.getInstance(cb.build());
        twitterBase = new TwitterBaseImpl(cb.build(), auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        assertNotNull(twitterBase.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        assertNotEquals(0L, twitterBase.getId());
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        User user = twitterBase.fillInIDAndScreenName();
        assertNotNull(user);
        assertNotNull(user.getScreenName());
        assertNotEquals(0L, user.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {}

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {}
        };
        twitterBase.addRateLimitStatusListener(listener);
        assertEquals(1, twitterBase.rateLimitStatusListeners.size());
    }

    @Test
    public void testOnRateLimitStatus() {
        //Implement test for onRateLimitStatus
    }

    @Test
    public void testOnRateLimitReached() {
        //Implement test for onRateLimitReached
    }

    @Test
    public void testHttpResponseReceived() {
        //Implement test for httpResponseReceived
    }

    @Test
    public void testGetAuthorization() {
        assertNotNull(twitterBase.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertNotNull(twitterBase.getConfiguration());
    }

    @Test
    public void testSetOAuthConsumer() {
        twitterBase.setOAuthConsumer("NEW_CONSUMER_KEY", "NEW_CONSUMER_SECRET");
        assertNotNull(twitterBase.getAuthorization());
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        assertNotNull(twitterBase.getOAuthRequestToken());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        assertNotNull(twitterBase.getOAuthAccessToken());
    }

    @Test
    public void testGetOAuthAccessTokenWithVerifier() throws TwitterException {
        String oauthVerifier = "VERIFIER_CODE";
        assertNotNull(twitterBase.getOAuthAccessToken(oauthVerifier));
    }

    @Test
    public void testGetOAuthAccessTokenWithRequestToken() throws TwitterException {
        RequestToken requestToken = twitterBase.getOAuthRequestToken();
        assertNotNull(twitterBase.getOAuthAccessToken(requestToken));
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken accessToken = new AccessToken("ACCESS_TOKEN", "ACCESS_TOKEN_SECRET");
        twitterBase.setOAuthAccessToken(accessToken);
        assertNotNull(twitterBase.getAuthorization());
    }

    @Test
    public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
        String screenName = "SCREEN_NAME";
        String password = "PASSWORD";
        assertNotNull(twitterBase.getOAuthAccessToken(screenName, password));
    }
}