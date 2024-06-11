import org.junit.Before;
import org.junit.Test;
import twitter4j.*;

import java.util.function.Consumer;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setup() {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();

        Authorization auth = new OAuthAuthorization(conf);

        twitterBaseImpl = new TwitterBaseImpl(conf, auth) {};
    }

    @Test
    public void testAddRateLimitStatusListener() {
        // Arrange
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {}

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {}
        };

        // Act
        twitterBaseImpl.addRateLimitStatusListener(listener);

        // Assert
        assertEquals(1, twitterBaseImpl.rateLimitStatusListeners.size());
        assertEquals(listener, twitterBaseImpl.rateLimitStatusListeners.get(0));
    }

    @Test
    public void testGetConfiguration() {
        // Act
        Configuration configuration = twitterBaseImpl.getConfiguration();

        // Assert
        assertNotNull(configuration);
        assertEquals(twitterBaseImpl.conf, configuration);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuthorization() throws TwitterException, IllegalStateException {
        // Act
        twitterBaseImpl.getId();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuthorization() throws TwitterException, IllegalStateException {
        // Act
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testHttpResponseReceived() {
        // Arrange
        HttpResponseEvent event = new HttpResponseEvent(twitterBaseImpl, new HttpResponse(), null);

        // Act
        twitterBaseImpl.httpResponseReceived(event);

        // Assert
        // Add assertion checks for the expected behavior
    }

    @Test
    public void testOnRateLimitReached() {
        // Arrange
        Consumer<RateLimitStatusEvent> action = event -> {
            // Do something with the event
        };

        // Act
        twitterBaseImpl.onRateLimitReached(action);

        // Assert
        assertEquals(1, twitterBaseImpl.rateLimitStatusListeners.size());
        // Add assertion checks for the expected behavior
    }

    @Test
    public void testOnRateLimitStatus() {
        // Arrange
        Consumer<RateLimitStatusEvent> action = event -> {
            // Do something with the event
        };

        // Act
        twitterBaseImpl.onRateLimitStatus(action);

        // Assert
        assertEquals(1, twitterBaseImpl.rateLimitStatusListeners.size());
        // Add assertion checks for the expected behavior
    }
}