import static org.junit.Assert.*;
import org.junit.Test;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructorWithMessage() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("Test message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals("java.lang.Exception: Test cause", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructorWithMessageAndHttpResponse() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(404);
        TwitterException exception = new TwitterException("Test message", response);
        assertEquals("404: The URI requested is invalid or the resource requested, such as a user, does not exists.\nTest message", exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test message", cause, 500);
        assertEquals("Test message", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWithNonExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        exception.getRetryAfter();
    }
    
    @Test
    public void testGetRetryAfterWithExceededRateLimitation() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test message", response);
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.isCausedByNetworkIssue());
        
        Exception cause = new IOException();
        exception = new TwitterException("Test message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.exceededRateLimitation());
        
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(400);
        response.setResponseHeader("x-rate-limit-remaining", "0");
        TwitterException rateLimitException = new TwitterException("Rate limit exceeded", response);
        assertTrue(rateLimitException.exceededRateLimitation());
        
        HttpResponse response2 = new HttpResponseImpl();
        response2.setStatusCode(420);
        TwitterException rateLimitException2 = new TwitterException("Rate limit exceeded", response2);
        assertTrue(rateLimitException2.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(404);
        TwitterException notFoundException = new TwitterException("Resource not found", response);
        assertTrue(notFoundException.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        assertNotNull(exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test message");
        exception.setNested();
        assertTrue(exception.equals(exception));
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message");
        TwitterException exception2 = new TwitterException("Test message");
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test message");
        assertNotNull(exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        assertNotNull(exception.toString());
    }
    
    private class HttpResponseImpl implements HttpResponse {
        private int statusCode;
        private String responseHeader;
        
        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
        
        public int getStatusCode() {
            return statusCode;
        }
        
        public void setResponseHeader(String name, String value) {
            this.responseHeader = name + ": " + value;
        }
        
        public String getResponseHeader(String name) {
            return responseHeader;
        }
    }
}