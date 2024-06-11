import org.junit.*;
import static org.junit.Assert.*;
import twitter4j.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitter;

    @Before
    public void setup() {
        Configuration conf = new ConfigurationBuilder().setDebugEnabled(true).build();
        Authorization auth = new NullAuthorization();
        twitter = new TwitterBaseImpl(conf, auth) {
            public void updateStatus(final String status) throws TwitterException {
                // This method is not tested
            }
        };
    }

    @Test
    public void testGetScreenName_basicAuthEnabled() throws TwitterException {
        // Test when BasicAuthorization is enabled
        BasicAuthorization auth = new BasicAuthorization("", "");
        twitter.setOAuthConsumer("", "");
        twitter.setAuthorization(auth);
        
        try {
            twitter.getScreenName();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException ex) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", ex.getMessage());
        }
    }

    @Test
    public void testGetScreenName_oAuthApplicationOnlyAuthEnabled() throws TwitterException {
        // Test when OAuth Application Only Authentication is enabled
        Configuration conf = new ConfigurationBuilder().setDebugEnabled(true)
            .setApplicationOnlyAuthEnabled(true)
            .setOAuthConsumerKey("consumerKey")
            .setOAuthConsumerSecret("consumerSecret")
            .build();
        Authorization auth = new OAuth2Authorization(conf);
        twitter.setOAuthConsumer("consumerKey", "consumerSecret");
        twitter.setAuthorization(auth);

        try {
            twitter.getScreenName();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException ex) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", ex.getMessage());
        }
    }

    @Test
    public void testGetScreenName_oAuthEnabled() throws TwitterException {
        // Test when OAuth is enabled
        Configuration conf = new ConfigurationBuilder().setDebugEnabled(true)
            .setOAuthConsumerKey("consumerKey")
            .setOAuthConsumerSecret("consumerSecret")
            .setOAuthAccessToken("accessToken")
            .setOAuthAccessTokenSecret("accessTokenSecret")
            .build();
        Authorization auth = new OAuthAuthorization(conf);
        twitter.setOAuthConsumer("consumerKey", "consumerSecret");
        twitter.setAuthorization(auth);

        try {
            String screenName = twitter.getScreenName();
            assertNull(screenName);
        } catch (IllegalArgumentException ex) {
            fail("Should not throw IllegalStateException");
        }
    }

    @Test
    public void testGetId_authNotEnabled() throws TwitterException {
        // Test when authorization is not enabled
        Configuration conf = new ConfigurationBuilder().setDebugEnabled(true).build();
        twitter.setOAuthConsumer("", "");

        try {
            twitter.getId();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException ex) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", ex.getMessage());
        }
    }

    @Test
    public void testGetId_authEnabled() throws TwitterException {
        // Test when authorization is enabled
        Configuration conf = new ConfigurationBuilder().setDebugEnabled(true)
            .setOAuthConsumerKey("consumerKey")
            .setOAuthConsumerSecret("consumerSecret")
            .setOAuthAccessToken("accessToken")
            .setOAuthAccessTokenSecret("accessTokenSecret")
            .build();
        Authorization auth = new OAuthAuthorization(conf);
        twitter.setOAuthConsumer("consumerKey", "consumerSecret");
        twitter.setAuthorization(auth);

        try {
            long id = twitter.getId();
            assertEquals(0, id);
        } catch (IllegalArgumentException ex) {
            fail("Should not throw IllegalStateException");
        }
    }
    
    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        // Test when fillInIDAndScreenName is called
        HttpParameter[] parameters = { new HttpParameter("param1", "value1"), new HttpParameter("param2", "value2") };
        User user = twitter.fillInIDAndScreenName(parameters);

        assertNotNull(user);
        assertEquals("screenName", user.getScreenName());
        assertEquals(12345, user.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        // Test adding RateLimitStatusListener
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            public void onRateLimitStatus(final RateLimitStatusEvent event) {}
            public void onRateLimitReached(final RateLimitStatusEvent event) {}
        };
        twitter.addRateLimitStatusListener(listener);

        assertEquals(1, twitter.rateLimitStatusListeners.size());
    }

    @After
    public void teardown() {
        twitter = null;
    }
}