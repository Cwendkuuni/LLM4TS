import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Custom exception message");
        assertEquals("Custom exception message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", exception.toString());
    }
    
    @Test
    public void testTwitterExceptionWithCause() {
        Exception innerException = new Exception("Inner exception");
        TwitterException exception = new TwitterException(innerException);
        assertEquals(innerException.toString(), exception.getCause().toString());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", exception.toString());
    }
    
    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.addResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Custom exception message", response);
        assertTrue(exception.getMessage().contains("Custom exception message"));
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("TwitterException{exceptionCode=[], statusCode=420, retryAfter=60, rateLimitStatus=null, version=null}", exception.toString());
    }
    
    @Test
    public void testTwitterExceptionWithMessageAndCause() {
        Exception innerException = new Exception("Inner exception");
        TwitterException exception = new TwitterException("Custom exception message", innerException);
        assertEquals("Custom exception message", exception.getMessage());
        assertEquals(innerException.toString(), exception.getCause().toString());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", exception.toString());
    }
    
    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        Exception innerException = new Exception("Inner exception");
        TwitterException exception = new TwitterException("Custom exception message", innerException, 404);
        assertEquals("Custom exception message", exception.getMessage());
        assertEquals(innerException.toString(), exception.getCause().toString());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertTrue(exception.resourceNotFound());
        assertEquals("TwitterException{exceptionCode=[], statusCode=404, retryAfter=0, rateLimitStatus=null, version=null}", exception.toString());
    }
    
    @Test
    public void testGetRetryAfter() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.addResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Custom exception message", response);
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeNot420() {
        TwitterException exception = new TwitterException("Custom exception message");
        exception.getRetryAfter();
    }
    
    @Test
    public void testEqualsAndHashCode() {
        TwitterException exception1 = new TwitterException("Exception 1");
        TwitterException exception2 = new TwitterException("Exception 2");
        
        // Test for equality
        assertTrue(exception1.equals(exception1));
        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals("string"));
        assertFalse(exception1.equals(exception2));
        
        // Test for hash code
        assertEquals(exception1.hashCode(), exception1.hashCode());
    }
}