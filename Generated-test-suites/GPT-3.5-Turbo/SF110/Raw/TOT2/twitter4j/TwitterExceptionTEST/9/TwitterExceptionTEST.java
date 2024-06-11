import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals(-1, twitterException.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("Test message");
        assertThrows(IllegalStateException.class, twitterException::getRetryAfter);
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException twitterException = new TwitterException(new IOException("Test exception"));
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException1 = new TwitterException("Test message", null, 400);
        assertTrue(twitterException1.exceededRateLimitation());

        TwitterException twitterException2 = new TwitterException("Test message", null, 420);
        assertTrue(twitterException2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test message");
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNotNull(twitterException.getExceptionCode());
    }

}