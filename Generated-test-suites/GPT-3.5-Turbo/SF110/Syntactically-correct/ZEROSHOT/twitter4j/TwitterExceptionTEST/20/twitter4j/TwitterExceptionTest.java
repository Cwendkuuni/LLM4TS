import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testTwitterExceptionString() {
        TwitterException exception = new TwitterException("test message");
        assertEquals("test message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception("test cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause.getMessage(), exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testTwitterExceptionStringHttpResponse() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("test message", res);
        assertEquals("test message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testTwitterExceptionStringException() {
        Exception cause = new Exception("test cause");
        TwitterException exception = new TwitterException("test message", cause);
        assertEquals("test message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception cause = new Exception("test cause");
        TwitterException exception = new TwitterException("test message", cause, 500);
        assertEquals("test message", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("test message");
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("test message");
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("test message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("test message");
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception1 = new TwitterException("test message");
        assertFalse(exception1.isCausedByNetworkIssue());
        
        Exception cause = new IOException("test io exception");
        TwitterException exception2 = new TwitterException(cause);
        assertTrue(exception2.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("test message");
        assertFalse(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("test message");
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("test message");
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("test message");
        exception.setNested();
        assertTrue(exception.nested);
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("test message");
        TwitterException exception2 = new TwitterException("test message");
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("test message");
        assertEquals(exception.statusCode, exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("test message");
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expected, exception.toString());
    }
}