import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("Test");
        
        assertEquals(-1, twitterException.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test");
        
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test");
        
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("Test");
        
        try {
            twitterException.getRetryAfter();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException twitterException = new TwitterException("Test", new IOException());
        
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException = new TwitterException("Test");
        twitterException.getStatusCode = 420;
        
        assertTrue(twitterException.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test");
        twitterException.getStatusCode = 404;
        
        assertTrue(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test");
        
        assertNotNull(twitterException.getExceptionCode());
    }
}