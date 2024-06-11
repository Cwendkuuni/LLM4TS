import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    // Test public methods in TwitterException

    @Test
    public void testConstructorWithMsg() {
        TwitterException exception = new TwitterException("Test message");
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Cause exception");
        TwitterException exception = new TwitterException(cause);
        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMsgAndHttpResponse() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");

        TwitterException exception = new TwitterException("Test message", res);
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testConstructorWithMsgAndCause() {
        Exception cause = new Exception("Cause exception");
        TwitterException exception = new TwitterException("Test message", cause);
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        Exception cause = new Exception("Cause exception");
        TwitterException exception = new TwitterException("Test message", cause, 500);
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
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
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test message");
        try {
            exception.getRetryAfter();
            fail("Expected exception not thrown");
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.isCausedByNetworkIssue());
        
        exception = new TwitterException(new IOException());
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.exceededRateLimitation());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(400);
        exception = new TwitterException("Test message", res);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(404);
        exception = new TwitterException("Test message", res);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("", exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message");
        TwitterException exception2 = new TwitterException("Test message");
        assertEquals(exception1, exception2);
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        exception1 = new TwitterException("Test message", res);
        exception2 = new TwitterException("Test message", res);
        assertEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test message", new IOException());
        exception2 = new TwitterException("Test message", new IOException());
        assertEquals(exception1, exception2);
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test message");
        TwitterException exception2 = new TwitterException("Test message");
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        exception1 = new TwitterException("Test message", res);
        exception2 = new TwitterException("Test message", res);
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=}";
        assertEquals(expected, exception.toString());
    }

    // Helper classes

    private class HttpResponseImpl implements HttpResponse {
        
        private int statusCode;
        private String responseHeader;

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public void setResponseHeader(String key, String value) {
            this.responseHeader = value;
        }

        @Override
        public int getStatusCode() {
            return this.statusCode;
        }

        @Override
        public String getResponseHeader(String name) {
            return this.responseHeader;
        }
    }
}

