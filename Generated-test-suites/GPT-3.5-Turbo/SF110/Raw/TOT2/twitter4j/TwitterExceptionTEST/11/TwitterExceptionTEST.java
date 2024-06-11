import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class TwitterExceptionTest {
    
    private TwitterException twitterException;
    
    @Before
    public void setUp() {
        twitterException = new TwitterException("Test Exception");
    }
    
    @Test
    public void testGetStatusCode() {
        assertEquals(-1, twitterException.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        try {
            twitterException.getRetryAfter();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Pass
        }
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        assertFalse(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        assertNotNull(twitterException.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException twitterException2 = new TwitterException("Test Exception");
        assertTrue(twitterException.equals(twitterException2));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(961, twitterException.hashCode());
    }
    
    @Test
    public void testToString() {
        assertNotNull(twitterException.toString());
    }
}