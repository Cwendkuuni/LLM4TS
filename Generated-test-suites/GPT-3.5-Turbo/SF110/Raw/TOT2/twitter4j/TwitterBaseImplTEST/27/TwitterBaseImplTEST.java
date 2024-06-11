import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.util.function.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        Configuration conf = new ConfigurationBuilder()
            .setOAuthConsumerKey("consumerKey")
            .setOAuthConsumerSecret("consumerSecret")
            .build();
        Authorization auth = new BasicAuthorization("userId", "password");
        twitterBase = new TwitterBaseImpl(conf, auth) {
            @Override
            void setFactory() {
                this.factory = new JSONImplFactory(this.conf);
            }
        };
    }

    @Test
    public void testGetScreenName() {
        try {
            String screenName = twitterBase.getScreenName();
            assertNotNull(screenName);
        } catch (TwitterException | IllegalStateException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetId() {
        try {
            long id = twitterBase.getId();
            assertNotEquals(0, id);
        } catch (TwitterException | IllegalStateException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testFillInIDAndScreenName() {
        try {
            User user = twitterBase.fillInIDAndScreenName();
            assertNotNull(user);
            assertNotNull(user.getScreenName());
            assertNotEquals(0, user.getId());
        } catch (TwitterException e) {
            fail("TwitterException thrown");
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = event -> {};
        twitterBase.addRateLimitStatusListener(listener);
        assertEquals(1, twitterBase.rateLimitStatusListeners.size());
    }

    // Additional test cases for other methods can be added here

    @After
    public void tearDown() {
        twitterBase = null;
    }
}