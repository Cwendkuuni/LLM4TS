
package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new NullAuthorization();

        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth) {
        };

        try {
            twitterBase.getScreenName();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Neither user ID/password combination nor OAuth consumer key/secret combination supplied"));
        }

        BasicAuthorization basicAuthorization = new BasicAuthorization("userId", "password");
        twitterBase.auth = basicAuthorization;

        try {
            twitterBase.getScreenName();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Neither user ID/password combination nor OAuth consumer key/secret combination supplied"));
        }

        OAuthAuthorization oauthAuthorization = new OAuthAuthorization(conf);
        twitterBase.auth = oauthAuthorization;

        try {
            twitterBase.getScreenName();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Authentication credentials are missing"));
            assertTrue(e.getMessage().contains("http://twitter4j.org/en/configuration.html"));
        }
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new NullAuthorization();

        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth) {
        };

        try {
            twitterBase.getId();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Neither user ID/password combination nor OAuth consumer key/secret combination supplied"));
        }

        BasicAuthorization basicAuthorization = new BasicAuthorization("userId", "password");
        twitterBase.auth = basicAuthorization;

        try {
            twitterBase.getId();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Neither user ID/password combination nor OAuth consumer key/secret combination supplied"));
        }

        OAuthAuthorization oauthAuthorization = new OAuthAuthorization(conf);
        twitterBase.auth = oauthAuthorization;

        try {
            twitterBase.getId();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Authentication credentials are missing"));
            assertTrue(e.getMessage().contains("http://twitter4j.org/en/configuration.html"));
        }
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new ConfigurationBuilder().setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setRestBaseURL("https://api.twitter.com/1.1/")
                .build();
        Authorization auth = new NullAuthorization();

        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth) {
        };

        try {
            twitterBase.fillInIDAndScreenName();
            fail("Should throw TwitterException");
        } catch (TwitterException e) {
            assertTrue(e.getMessage().contains("Authorization credentials are missing"));
            assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new ConfigurationBuilder().setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new NullAuthorization();

        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth) {
        };

        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {

            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {

            }
        };

        twitterBase.addRateLimitStatusListener(listener);

        assertFalse(twitterBase.rateLimitStatusListeners.isEmpty());
        assertTrue(twitterBase.rateLimitStatusListeners.contains(listener));
    }
    
    // Add more test methods here...

}
