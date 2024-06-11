
import org.junit.*;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {
    @Test
    public void testGetScreenName() {
        // Initialize
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth) {
            public User fillInIDAndScreenName() {
                return null;
            }
        };

        // Test
        try {
            String screenName = twitter.getScreenName();
            assertNull(screenName);
        } catch (TwitterException | IllegalStateException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetId() {
        // Initialize
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth) {
            public User fillInIDAndScreenName() {
                return null;
            }
        };

        // Test
        try {
            long id = twitter.getId();
            assertEquals(0L, id);
        } catch (TwitterException | IllegalStateException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEnsureAuthorizationEnabled() {
        // Initialize
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth) {
            public User fillInIDAndScreenName() {
                return null;
            }
        };

        // Test
        try {
            twitter.ensureAuthorizationEnabled();
        } catch (IllegalStateException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEnsureOAuthEnabled() {
        // Initialize
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth) {
            public User fillInIDAndScreenName() {
                return null;
            }
        };

        // Test
        try {
            twitter.ensureOAuthEnabled();
        } catch (IllegalStateException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
