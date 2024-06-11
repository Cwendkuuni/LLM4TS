
package twitter4j;

import twitter4j.conf.Configuration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {
    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new OAuthAuthorization(conf);
        twitterBase = new TwitterBaseImpl(conf, auth) {};
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        // Test when authentication is disabled
        twitterBase.auth.setEnabled(false);
        try {
            twitterBase.getScreenName();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }

        // Test when screen name is null
        twitterBase.auth.setEnabled(true);
        twitterBase.screenName = null;
        assertEquals(null, twitterBase.getScreenName());

        // Test when screen name is not null
        twitterBase.screenName = "testUser";
        assertEquals("testUser", twitterBase.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        // Test when authentication is disabled
        twitterBase.auth.setEnabled(false);
        try {
            twitterBase.getId();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }

        // Test when ID is 0
        twitterBase.auth.setEnabled(true);
        twitterBase.id = 0L;
        assertEquals(0L, twitterBase.getId());

        // Test when ID is not 0
        twitterBase.id = 12345L;
        assertEquals(12345L, twitterBase.getId());
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        // Test when parameters are null
        User user = twitterBase.fillInIDAndScreenName();
        assertEquals("user", user.getScreenName());

        // Test when parameters are not null
        HttpParameter[] parameters = {new HttpParameter("count", 100)};
        user = twitterBase.fillInIDAndScreenName(parameters);
        assertEquals("user", user.getScreenName());
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
        assertTrue(twitterBase.rateLimitStatusListeners.contains(listener));
    }
    
    // Test other methods in the class
    
}

