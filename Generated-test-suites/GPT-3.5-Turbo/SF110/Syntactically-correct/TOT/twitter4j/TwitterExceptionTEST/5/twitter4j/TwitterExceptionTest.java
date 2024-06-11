import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructorWithMessage() {
        TwitterException exception = new TwitterException("Test Exception");
        
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithCause() {
        RuntimeException cause = new RuntimeException("Test Cause");
        TwitterException exception = new TwitterException(cause);
        
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageAndResponse() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        
        TwitterException exception = new TwitterException("Test Message", response);
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("Test Cause");
        TwitterException exception = new TwitterException("Test Message", cause);
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        RuntimeException cause = new RuntimeException("Test Cause");
        TwitterException exception = new TwitterException("Test Message", cause, 500);
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenNotExceededRateLimit() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        Exception cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException("Test Exception", cause);
        
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.exceededRateLimitation());
        
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        exception = new TwitterException("Test Exception", response);
        assertTrue(exception.exceededRateLimitation());
        
        response.setStatusCode(400);
        exception = new TwitterException("Test Exception", response);
        assertTrue(exception.exceededRateLimitation());
        
        response.setStatusCode(500);
        exception = new TwitterException("Test Exception", response);
        assertFalse(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(404);
        exception = new TwitterException("Test Exception", response);
        assertTrue(exception.resourceNotFound());
        
        response.setStatusCode(500);
        exception = new TwitterException("Test Exception", response);
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        String exceptionCode = exception.getExceptionCode();
        assertNotNull(exceptionCode);
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.setNested();
        
        assertTrue(exception.isNested());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception 1");
        TwitterException exception2 = new TwitterException("Test Exception 2");
        
        assertFalse(exception1.equals(exception2));
        assertFalse(exception1.equals(null));
        assertTrue(exception1.equals(exception1));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Exception");
        
        int hashCode = exception.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        
        assertNotNull(exception.toString());
    }
}