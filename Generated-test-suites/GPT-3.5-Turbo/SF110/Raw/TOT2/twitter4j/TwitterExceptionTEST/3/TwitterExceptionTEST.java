
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("Test Message");
        twitterException = twitterException;
        assertEquals(-1, twitterException.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Message");
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Message");
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("Test Message");
        try {
            twitterException.getRetryAfter();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException();
        TwitterException twitterException = new TwitterException(ioException);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException1 = new TwitterException("Test Message", null, 400);
        assertTrue(twitterException1.exceededRateLimitation());

        TwitterException twitterException2 = new TwitterException("Test Message", null, 420);
        assertTrue(twitterException2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test Message", null, 404);
        assertTrue(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test Message");
        assertEquals("TwitterException", twitterException.getExceptionCode());
    }

}
