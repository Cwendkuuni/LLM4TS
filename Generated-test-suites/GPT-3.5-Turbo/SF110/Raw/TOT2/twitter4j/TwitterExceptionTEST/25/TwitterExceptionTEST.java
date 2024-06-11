import org.junit.jupiter.api.Test;
import twitter4j.TwitterException;
import static org.junit.jupiter.api.Assertions.*;

public class TwitterExceptionTest {
    
    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("Test Exception");
        assertEquals(-1, twitterException.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Exception");
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Exception");
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("Test Exception");
        try {
            twitterException.getRetryAfter();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException twitterException = new TwitterException("Test Exception", new java.io.IOException());
        assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException = new TwitterException("Test Exception", null, 420);
        assertTrue(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test Exception", null, 404);
        assertTrue(twitterException.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test Exception");
        assertNotNull(twitterException.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test Exception");
        TwitterException twitterException2 = new TwitterException("Test Exception");
        assertTrue(twitterException1.equals(twitterException2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test Exception");
        assertEquals(0, twitterException.hashCode());  // hash code is calculated based on fields so initial value will be 0
    }
    
    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("Test Exception");
        assertNotNull(twitterException.toString());
    }

}