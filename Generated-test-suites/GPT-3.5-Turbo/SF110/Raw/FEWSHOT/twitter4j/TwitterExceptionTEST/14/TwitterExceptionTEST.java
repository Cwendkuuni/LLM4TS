
package twitter4j;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TwitterExceptionTest {
    
    private TwitterException twitterException;
    
    @Before
    public void setUp() {
        twitterException = new TwitterException("test message");
    }
    
    @Test
    public void testConstructorWithMessage() {
        assertEquals("test message", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("test exception");
        twitterException = new TwitterException(cause);
        assertEquals("test exception", twitterException.getCause().getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageAndResponse() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        response.setHeader("Retry-After", "60");
        twitterException = new TwitterException("test message", response);
        assertEquals("test message\n420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", twitterException.getMessage());
        assertEquals(420, twitterException.getStatusCode());
        assertNotNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertEquals(60, twitterException.getRetryAfter());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        Exception cause = new Exception("test exception");
        twitterException = new TwitterException("test message", cause);
        assertEquals("test message", twitterException.getMessage());
        assertEquals("test exception", twitterException.getCause().getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("test exception");
        twitterException = new TwitterException("test message", cause, 500);
        assertEquals("test message", twitterException.getMessage());
        assertEquals("test exception", twitterException.getCause().getMessage());
        assertEquals(500, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        assertEquals(-1, twitterException.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        twitterException.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        Exception cause = new IOException();
        twitterException = new TwitterException("test message", cause);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(400);
        twitterException = new TwitterException("test message", response);
        assertTrue(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(404);
        twitterException = new TwitterException("test message", response);
        assertTrue(twitterException.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        assertEquals("1BC:lazM", twitterException.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        assertTrue(twitterException.equals(twitterException));
        assertFalse(twitterException.equals(null));
        assertFalse(twitterException.equals(new Object()));
        TwitterException twitterException2 = new TwitterException("test message");
        assertTrue(twitterException.equals(twitterException2));
        twitterException2.setNested();
        assertFalse(twitterException.equals(twitterException2));
        twitterException2.setNested();
        assertFalse(twitterException.equals(twitterException2));
        
        twitterException = new TwitterException("test message", new IOException(), 500);
        twitterException2 = new TwitterException("test message", new IOException(), 500);
        assertTrue(twitterException.equals(twitterException2));
        twitterException2.setNested();
        assertFalse(twitterException.equals(twitterException2));
        twitterException2.setNested();
        assertFalse(twitterException.equals(twitterException2));
        
        twitterException = new TwitterException("test message", new IOException(), 500);
        twitterException2 = new TwitterException("test message", new IOException(), 404);
        assertFalse(twitterException.equals(twitterException2));
        
        TwitterException twitterException3 = new TwitterException("test message");
        assertTrue(twitterException.equals(twitterException3));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, twitterException.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=4.0.4}", twitterException.toString());
    }
    
    private class HttpResponseImpl implements HttpResponse {

        private int statusCode;
        private String header;
        
        @Override
        public RateLimitStatus getRateLimitStatus() {
            return null;
        }

        @Override
        public int getStatusCode() {
            return statusCode;
        }

        @Override
        public String getResponseHeader(String name) {
            return header;
        }
        
        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
        
        public void setHeader(String name, String value) {
            this.header = value;
        }
        
    }
    
}

