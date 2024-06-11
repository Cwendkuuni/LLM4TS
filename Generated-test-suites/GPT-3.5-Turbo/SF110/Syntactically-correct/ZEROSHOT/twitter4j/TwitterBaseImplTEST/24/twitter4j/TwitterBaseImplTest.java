import org.junit.*;
import twitter4j.auth.*;
import twitter4j.conf.Configuration;
import twitter4j.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        twitterBase = new TwitterBaseImpl(conf, auth) {
        };
    }

    @After
    public void tearDown() {
        twitterBase = null;
    }

    @Test
    public void testGetScreenName() {
        try {
            String screenName = twitterBase.getScreenName();
            Assert.assertNotNull(screenName);
        } catch (TwitterException e) {
            Assert.fail(e.getMessage());
        } catch (IllegalStateException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetId() {
        try {
            long id = twitterBase.getId();
            Assert.assertNotEquals(0L, id);
        } catch (TwitterException e) {
            Assert.fail(e.getMessage());
        } catch (IllegalStateException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        };
        twitterBase.addRateLimitStatusListener(listener);
        List<RateLimitStatusListener> listeners = twitterBase.getRateLimitStatusListeners();
        Assert.assertFalse(listeners.isEmpty());
        Assert.assertEquals(1, listeners.size());
        Assert.assertEquals(listener, listeners.get(0));
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
            @Override
            public void accept(RateLimitStatusEvent event) {
            }
        };
        twitterBase.onRateLimitStatus(action);
        List<RateLimitStatusListener> listeners = twitterBase.getRateLimitStatusListeners();
        Assert.assertFalse(listeners.isEmpty());
        Assert.assertEquals(1, listeners.size());
        Assert.assertNotNull(listeners.get(0));
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
            @Override
            public void accept(RateLimitStatusEvent event) {
            }
        };
        twitterBase.onRateLimitReached(action);
        List<RateLimitStatusListener> listeners = twitterBase.getRateLimitStatusListeners();
        Assert.assertFalse(listeners.isEmpty());
        Assert.assertEquals(1, listeners.size());
        Assert.assertNotNull(listeners.get(0));
    }

    @Test
    public void testGetAuthorization() {
        Authorization auth = twitterBase.getAuthorization();
        Assert.assertNotNull(auth);
    }

    @Test
    public void testGetConfiguration() {
        Configuration conf = twitterBase.getConfiguration();
        Assert.assertNotNull(conf);
    }
}