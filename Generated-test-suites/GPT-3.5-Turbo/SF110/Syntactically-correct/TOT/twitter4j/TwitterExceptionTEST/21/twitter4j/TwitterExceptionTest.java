import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageAndResponse() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(404);
        TwitterException exception = new TwitterException("Test message", res);
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test message", cause, 420);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(420, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals(-1, exception.getStatusCode());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(404);
        exception = new TwitterException("Test message", res);
        assertEquals(404, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        assertNull(exception.getRateLimitStatus());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        exception = new TwitterException("Test message", res);
        assertNull(exception.getRateLimitStatus());
        
        res.setStatusCode(420);
        exception = new TwitterException("Test message", res);
        assertNotNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        exception = new TwitterException("Test message", res);
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        
        res.setStatusCode(420);
        exception = new TwitterException("Test message", res);
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test message");
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(404);
        exception = new TwitterException("Test message", res);
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
        
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        exception = new TwitterException("Test message", res);
        assertEquals(60, exception.getRetryAfter());
        
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "invalid");
        exception = new TwitterException("Test message", res);
        try {
            exception.getRetryAfter();
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Do nothing, exception is expected
        }
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.isCausedByNetworkIssue());
        
        Exception cause = new IOException("Test IO exception");
        exception = new TwitterException("Test message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.exceededRateLimitation());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        exception = new TwitterException("Test message", res);
        assertFalse(exception.exceededRateLimitation());
        
        res.setStatusCode(400);
        exception = new TwitterException("Test message", res);
        assertTrue(exception.exceededRateLimitation());
        
        res.setStatusCode(420);
        exception = new TwitterException("Test message", res);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        exception = new TwitterException("Test message", res);
        assertFalse(exception.resourceNotFound());
        
        res.setStatusCode(404);
        exception = new TwitterException("Test message", res);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        assertNotNull(exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message");
        assertTrue(exception1.equals(exception1)); // Same instance
        
        TwitterException exception2 = new TwitterException("Test message");
        assertTrue(exception1.equals(exception2)); // Same content
        
        HTTPResponse res = new HTTPResponseImpl();
        res.setStatusCode(404);
        exception2 = new TwitterException("Test message", res);
        assertFalse(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test message");
        TwitterException exception2 = new TwitterException("Test message");
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        HTTPResponse res = new HTTPResponseImpl();
        res.setStatusCode(404);
        exception2 = new TwitterException("Test message", res);
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        assertNotNull(exception.toString());
    }
}