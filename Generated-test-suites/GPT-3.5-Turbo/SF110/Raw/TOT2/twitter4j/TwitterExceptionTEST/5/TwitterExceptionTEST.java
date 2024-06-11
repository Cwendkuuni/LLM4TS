import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructorWithString() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals("Test message", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithException() {
        Exception cause = new Exception();
        TwitterException ex = new TwitterException(cause);
        assertEquals(cause, ex.getCause());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithStringAndHttpResponse() {
        HttpResponse res = new HttpResponse();
        TwitterException ex = new TwitterException("Test message", res);
        assertEquals("Test message\n", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNotNull(ex.getRateLimitStatus());
        assertNotNull(ex.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals(0, ex.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException ex = new TwitterException("Test message");
        assertFalse(ex.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException ex = new TwitterException("Test message");
        assertFalse(ex.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException ex = new TwitterException("Test message");
        assertFalse(ex.resourceNotFound());
    }
    
    @Test
    public void testEquals() {
        TwitterException ex1 = new TwitterException("Test message");
        TwitterException ex2 = new TwitterException("Test message");
        assertTrue(ex1.equals(ex2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals(0, ex.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=1.1}", ex.toString());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals("0000", ex.getExceptionCode());
    }
}