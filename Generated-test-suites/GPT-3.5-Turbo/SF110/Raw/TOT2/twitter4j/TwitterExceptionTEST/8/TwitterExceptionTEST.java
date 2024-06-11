import static org.junit.Assert.*;
import org.junit.Test;

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
        try {
            twitterException.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // expected
        }
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException twitterException = new TwitterException(new IOException());
        assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException1 = new TwitterException("Test message", null, 400);
        TwitterException twitterException2 = new TwitterException("Test message", null, 420);
        assertTrue(twitterException1.exceededRateLimitation());
        assertTrue(twitterException2.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test message", null, 404);
        assertTrue(twitterException.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNotNull(twitterException.getExceptionCode());
    }
}