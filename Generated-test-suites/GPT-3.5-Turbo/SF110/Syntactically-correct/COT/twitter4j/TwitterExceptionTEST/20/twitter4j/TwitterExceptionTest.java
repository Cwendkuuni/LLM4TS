
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructorWithStringMessage() {
        String message = "Test exception";
        TwitterException exception = new TwitterException(message);
        assertEquals(message, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testConstructorWithException() {
        Exception cause = new IOException("Test exception");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testConstructorWithStringAndHttpResponse() {
        String message = "Test exception";
        HttpResponse response = new HttpResponseImpl();
        TwitterException exception = new TwitterException(message, response);
        assertEquals(message + "\n" + TwitterException.getCause(response), exception.getMessage());
        assertEquals(response.getStatusCode(), exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNotNull(exception.getExceptionCode());
    }
    
    @Test
    public void testConstructorWithFullArguments() {
        String message = "Test exception";
        Exception cause = new IOException("Nested exception");
        int statusCode = 500;
        TwitterException exception = new TwitterException(message, cause, statusCode);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testGetRetryAfter() {
        int statusCode = 420;
        int retryAfter = 60;
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(statusCode);
        response.addResponseHeader("Retry-After", String.valueOf(retryAfter));
        TwitterException exception = new TwitterException("Test exception", response);
        try {
            int result = exception.getRetryAfter();
            assertEquals(retryAfter, result);
        } catch (IllegalStateException e) {
            fail("Expected to get retry after value");
        }
        
        statusCode = 200;
        response.setStatusCode(statusCode);
        exception = new TwitterException("Test exception", response);
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Pass
        }
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception");
        TwitterException exception2 = new TwitterException("Test exception");
        assertEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new IOException("Nested exception"));
        exception2 = new TwitterException("Test exception", new IOException("Nested exception"));
        assertEquals(exception1, exception2);
        
        HttpResponse response = new HttpResponseImpl();
        exception1 = new TwitterException("Test exception", response);
        exception2 = new TwitterException("Test exception", response);
        assertEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new Exception());
        exception2 = new TwitterException("Test exception", new Exception());
        assertEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new Exception(), 500);
        exception2 = new TwitterException("Test exception", new Exception(), 500);
        assertEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception");
        exception2 = new TwitterException("Different exception");
        assertNotEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception");
        exception2 = new TwitterException("Test exception", new IOException("Nested exception"));
        assertNotEquals(exception1, exception2);
        
        response = new HttpResponseImpl();
        exception1 = new TwitterException("Test exception", response);
        response = new HttpResponseImpl();
        exception2 = new TwitterException("Test exception", response);
        assertNotEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new Exception(), 500);
        exception2 = new TwitterException("Test exception", new Exception(), 200);
        assertNotEquals(exception1, exception2);
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception");
        TwitterException exception2 = new TwitterException("Test exception");
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new IOException("Nested exception"));
        exception2 = new TwitterException("Test exception", new IOException("Nested exception"));
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        HttpResponse response = new HttpResponseImpl();
        exception1 = new TwitterException("Test exception", response);
        exception2 = new TwitterException("Test exception", response);
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new Exception());
        exception2 = new TwitterException("Test exception", new Exception());
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new Exception(), 500);
        exception2 = new TwitterException("Test exception", new Exception(), 500);
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception");
        exception2 = new TwitterException("Different exception");
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception");
        exception2 = new TwitterException("Test exception", new IOException("Nested exception"));
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        
        response = new HttpResponseImpl();
        exception1 = new TwitterException("Test exception", response);
        response = new HttpResponseImpl();
        exception2 = new TwitterException("Test exception", response);
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new Exception(), 500);
        exception2 = new TwitterException("Test exception", new Exception(), 200);
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test exception");
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=unknown}";
        assertEquals(expected, exception.toString());
        
        HttpResponse response = new HttpResponseImpl();
        exception = new TwitterException("Test exception", response);
        expected = "TwitterException{exceptionCode=[], statusCode=" + response.getStatusCode() + ", retryAfter=0, rateLimitStatus=RateLimitStatusJSONImpl{remaining=0, limit=0, resetTimeInSeconds=0, resetTime=Wed Dec 31 16:00:00 EST 1969}, version=unknown}";
        assertEquals(expected, exception.toString());
        
        response.setStatusCode(420);
        response.addResponseHeader("Retry-After", "60");
        exception = new TwitterException("Test exception", response);
        expected = "TwitterException{exceptionCode=[], statusCode=" + response.getStatusCode() + ", retryAfter=60, rateLimitStatus=RateLimitStatusJSONImpl{remaining=0, limit=0, resetTimeInSeconds=0, resetTime=Wed Dec 31 16:00:00 EST 1969}, version=unknown}";
        assertEquals(expected, exception.toString());
        
        response.setStatusCode(500);
        response = new HttpResponseImpl();
        exception = new TwitterException("Test exception", response);
        expected = "TwitterException{exceptionCode=[], statusCode=" + response.getStatusCode() + ", retryAfter=0, rateLimitStatus=null, version=unknown}";
        assertEquals(expected, exception.toString());
    }
}
