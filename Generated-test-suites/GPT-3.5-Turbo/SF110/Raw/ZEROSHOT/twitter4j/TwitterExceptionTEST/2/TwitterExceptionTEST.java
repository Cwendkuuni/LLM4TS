import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMsg() {
        TwitterException exception = new TwitterException("test message");
        assertEquals("test message", exception.getMessage());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("test cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals("test cause", exception.getCause().getMessage());
    }
    
    @Test
    public void testConstructorWithMsgAndRes() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        TwitterException exception = new TwitterException("test message", res);
        assertEquals("test message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
    }
    
    @Test
    public void testConstructorWithMsgAndCause() {
        Exception cause = new Exception("test cause");
        TwitterException exception = new TwitterException("test message", cause);
        assertEquals("test message", exception.getMessage());
        assertEquals("test cause", exception.getCause().getMessage());
    }
    
    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        Exception cause = new Exception("test cause");
        TwitterException exception = new TwitterException("test message", cause, 500);
        assertEquals("test message", exception.getMessage());
        assertEquals("test cause", exception.getCause().getMessage());
        assertEquals(500, exception.getStatusCode());
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
        TwitterException exception = new TwitterException("test message");
        assertFalse(exception.isCausedByNetworkIssue());
        
        exception = new TwitterException(new IOException("Network issue"));
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("test message");
        assertFalse(exception.exceededRateLimitation());
        
        exception = new TwitterException("test message", null, 400);
        assertFalse(exception.exceededRateLimitation());
        
        RateLimitStatus rateLimitStatus = new RateLimitStatusImpl();
        exception = new TwitterException("test message", null, 400);
        exception.rateLimitStatus = rateLimitStatus;
        assertTrue(exception.exceededRateLimitation());
        
        exception = new TwitterException("test message", null, 420);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("test message");
        assertFalse(exception.resourceNotFound());
        
        exception = new TwitterException("test message", null, 404);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("test message");
        assertEquals("", exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("test message");
        TwitterException exception2 = new TwitterException("test message");
        assertTrue(exception1.equals(exception2));
        
        exception1.statusCode = 500;
        assertFalse(exception1.equals(exception2));
        
        exception2.statusCode = 500;
        assertTrue(exception1.equals(exception2));
        
        exception1.retryAfter = 60;
        assertFalse(exception1.equals(exception2));
        
        exception2.retryAfter = 60;
        assertTrue(exception1.equals(exception2));
        
        RateLimitStatus rateLimitStatus = new RateLimitStatusImpl();
        exception1.rateLimitStatus = rateLimitStatus;
        assertFalse(exception1.equals(exception2));
        
        exception2.rateLimitStatus = rateLimitStatus;
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("test message");
        assertEquals(0, exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("test message");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", exception.toString());
    }
    
    public class HttpResponseImpl implements HttpResponse {
        private int statusCode;
        
        public int getStatusCode() {
            return statusCode;
        }
        
        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
        
        public String getResponseHeader(String name) {
            return null;
        }
    }
    
    public class RateLimitStatusImpl implements RateLimitStatus {
        public int getRemaining() {
            return 0;
        }
        
        public int getLimit() {
            return 0;
        }
        
        public int getResetTimeInSeconds() {
            return 0;
        }
    }

}