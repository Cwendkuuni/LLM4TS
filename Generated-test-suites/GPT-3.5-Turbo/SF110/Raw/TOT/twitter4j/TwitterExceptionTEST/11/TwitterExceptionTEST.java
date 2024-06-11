import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        // Typical use case
        TwitterException exception = new TwitterException("This is a test exception");
        assertEquals("This is a test exception", exception.getMessage());
        
        // Edge case: Empty string
        exception = new TwitterException("");
        assertEquals("", exception.getMessage());
        
        // Error scenario: Exception with cause
        Exception cause = new Exception("Cause of exception");
        exception = new TwitterException("Exception with cause", cause);
        assertEquals("Exception with cause", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    public void testTwitterExceptionException() {
        // Typical use case
        Exception cause = new Exception("Cause of exception");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        
        // Edge case: Exception of type TwitterException as cause
        TwitterException twitterException = new TwitterException("Nested exception");
        cause = new Exception(twitterException);
        exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getCause() instanceof TwitterException);
        
        // Error scenario: Exception with null cause
        exception = new TwitterException(null);
        assertNull(exception.getCause());
    }
    
    @Test
    public void testTwitterExceptionStringHttpResponse() {
        // Typical use case
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        TwitterException exception = new TwitterException("Exception with response", response);
        assertEquals("200:", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        
        // Edge case: Retry-After header present
        response = new HttpResponse();
        response.setStatusCode(420);
        response.addResponseHeader("Retry-After", "60");
        exception = new TwitterException("Exception with response and Retry-After", response);
        assertEquals("420:60", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        
        // Error scenario: Retry-After header not present
        response = new HttpResponse();
        response.setStatusCode(420);
        exception = new TwitterException("Exception with response and Retry-After", response);
        assertEquals("420:", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(-1, exception.getRetryAfter());
    }
    
    @Test
    public void testTwitterExceptionStringException() {
        // Typical use case
        Exception cause = new Exception("Cause of exception");
        TwitterException exception = new TwitterException("Exception with cause", cause);
        assertEquals("Exception with cause", exception.getMessage());
        assertEquals(cause, exception.getCause());
        
        // Edge case: Empty string message
        cause = new Exception("");
        exception = new TwitterException("", cause);
        assertEquals("", exception.getMessage());
        assertEquals(cause, exception.getCause());
        
        // Error scenario: Null cause
        exception = new TwitterException("Exception with null cause", null);
        assertEquals("Exception with null cause", exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    public void testTwitterExceptionStringExceptionInt() {
        // Typical use case
        Exception cause = new Exception("Cause of exception");
        TwitterException exception = new TwitterException("Exception with cause and status code", cause, 500);
        assertEquals("Exception with cause and status code", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        
        // Error scenario: Null cause
        exception = new TwitterException("Exception with null cause and status code", null, 400);
        assertEquals("Exception with null cause and status code", exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(400, exception.getStatusCode());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test exception");
        assertEquals(-1, exception.getStatusCode());
        
        exception = new TwitterException("Test exception", new HttpResponse());
        assertEquals(-1, exception.getStatusCode());
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        exception = new TwitterException("Test exception", response);
        assertEquals(200, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test exception");
        assertNull(exception.getRateLimitStatus());
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        exception = new TwitterException("Test exception", response);
        assertNotNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test exception");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        exception = new TwitterException("Test exception", response);
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test exception");
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        exception = new TwitterException("Test exception", response);
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
        
        response = new HttpResponse();
        response.setStatusCode(420);
        response.addResponseHeader("Retry-After", "60");
        exception = new TwitterException("Test exception", response);
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test exception");
        assertFalse(exception.isCausedByNetworkIssue());
        
        exception = new TwitterException(new IOException("Network issue"));
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test exception");
        assertFalse(exception.exceededRateLimitation());
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        exception = new TwitterException("Test exception", response);
        assertTrue(exception.exceededRateLimitation());
        
        response = new HttpResponse();
        response.setStatusCode(420);
        exception = new TwitterException("Test exception", response);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test exception");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(404);
        exception = new TwitterException("Test exception", response);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test exception");
        assertNotNull(exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception");
        TwitterException exception2 = new TwitterException("Test exception");
        assertTrue(exception1.equals(exception2));
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        exception1 = new TwitterException("Test exception", response);
        exception2 = new TwitterException("Test exception", response);
        assertTrue(exception1.equals(exception2));
        
        response = new HttpResponse();
        response.setStatusCode(200);
        exception1 = new TwitterException("Test exception", response);
        response = new HttpResponse();
        response.setStatusCode(200);
        exception2 = new TwitterException("Test exception", response);
        assertTrue(exception1.equals(exception2));
        
        response = new HttpResponse();
        response.setStatusCode(200);
        exception1 = new TwitterException("Test exception", response);
        response = new HttpResponse();
        response.setStatusCode(400);
        exception2 = new TwitterException("Test exception", response);
        assertFalse(exception1.equals(exception2));
        
        exception1 = new TwitterException("Test exception");
        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals("String"));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception");
        TwitterException exception2 = new TwitterException("Test exception");
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        exception1 = new TwitterException("Test exception", response);
        exception2 = new TwitterException("Test exception", response);
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        response = new HttpResponse();
        response.setStatusCode(200);
        exception1 = new TwitterException("Test exception", response);
        response = new HttpResponse();
        response.setStatusCode(200);
        exception2 = new TwitterException("Test exception", response);
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        response = new HttpResponse();
        response.setStatusCode(200);
        exception1 = new TwitterException("Test exception", response);
        response = new HttpResponse();
        response.setStatusCode(400);
        exception2 = new TwitterException("Test exception", response);
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test exception");
        assertTrue(exception.toString().contains("exceptionCode=["));
        assertTrue(exception.toString().contains("statusCode="));
        assertTrue(exception.toString().contains("retryAfter="));
        assertTrue(exception.toString().contains("rateLimitStatus="));
        assertTrue(exception.toString().contains("version="));
    }
}