import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitter;

    @Before
    public void setup() {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();

        Authorization auth = new OAuthAuthorization(conf);

        twitter = new TwitterBaseImpl(conf, auth) {};
    }

    @Test
    public void testGetScreenName() {
        try {
            String screenName = twitter.getScreenName();
            assertNotNull(screenName);
        } catch (TwitterException | IllegalStateException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetId() {
        try {
            long id = twitter.getId();
            assertNotEquals(0L, id);
        } catch (TwitterException | IllegalStateException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) { }
            public void onRateLimitReached(RateLimitStatusEvent event) { }
        };

        twitter.addRateLimitStatusListener(listener);

        List<RateLimitStatusListener> listeners = twitter.getRateLimitStatusListeners();
        assertTrue(listeners.contains(listener));
    }

    @Test
    public void testGetAuthorization() {
        Authorization auth = twitter.getAuthorization();
        assertNotNull(auth);
    }

    @Test
    public void testGetConfiguration() {
        Configuration conf = twitter.getConfiguration();
        assertNotNull(conf);
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureAuthorizationEnabled() {
        twitter.ensureAuthorizationEnabled();
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureOAuthEnabled() {
        twitter.ensureOAuthEnabled();
    }

    // Add more test cases for other methods

}