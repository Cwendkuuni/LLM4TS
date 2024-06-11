import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructorWithMsg() {
        String msg = "Test message";
        TwitterException exception = new TwitterException(msg);
        
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructorWithException() {
        Exception cause = new Exception("Test exception");
        TwitterException exception = new TwitterException(cause);
        
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructorWithMsgAndHttpResponse() {
        String msg = "Test message";
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        res.setResponseHeader("Retry-After", "60");
        
        TwitterException exception = new TwitterException(msg, res);
        
        assertEquals(msg, exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructorWithMsgAndException() {
        String msg = "Test message";
        Exception cause = new Exception("Test exception");
        
        TwitterException exception = new TwitterException(msg, cause);
        
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructorWithMsgAndExceptionAndStatusCode() {
        String msg = "Test message";
        Exception cause = new Exception("Test exception");
        int statusCode = 400;
        
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeNot420() {
        TwitterException exception = new TwitterException("Test message");
        exception.getRetryAfter();
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        String exceptionCode = exception.getExceptionCode();
        
        assertNotNull(exceptionCode);
        assertTrue(exceptionCode.length() > 0);
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.isNested());
        
        exception.setNested();
        
        assertTrue(exception.isNested());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message 1");
        TwitterException exception2 = new TwitterException("Test message 2");
        TwitterException exception3 = new TwitterException("Test message 1");
        
        assertFalse(exception1.equals(exception2));
        assertTrue(exception1.equals(exception3));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test message 1");
        TwitterException exception2 = new TwitterException("Test message 2");
        TwitterException exception3 = new TwitterException("Test message 1");
        
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        assertEquals(exception1.hashCode(), exception3.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        
        String toString = exception.toString();
        
        assertNotNull(toString);
    }
}