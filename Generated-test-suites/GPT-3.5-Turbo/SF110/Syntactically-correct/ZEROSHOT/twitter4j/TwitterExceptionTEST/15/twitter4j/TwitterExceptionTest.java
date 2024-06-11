import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TwitterExceptionTest {
    
    private TwitterException exception;
    
    @Before
    public void setUp() {
        exception = new TwitterException("Test exception");
    }
    
    @Test
    public void testConstructorWithMsg() {
        assertEquals("Test exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Test cause");
        exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
    }
    
    @Test
    public void testConstructorWithMsgAndRes() {
        HttpResponse res = new HttpResponseImpl();
        exception = new TwitterException("Test exception", res);
        assertEquals("Test exception", exception.getMessage());
        assertEquals(res.getStatusCode(), exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertFalse(exception.exceededRateLimitation());
    }
    
    @Test
    public void testConstructorWithMsgAndCause() {
        Exception cause = new Exception("Test cause");
        exception = new TwitterException("Test exception", cause);
        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        Exception cause = new Exception("Test cause");
        exception = new TwitterException("Test exception", cause, 500);
        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        exception = new TwitterException("Test exception");
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        assertEquals(-1, exception.getRetryAfter());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(420, "60"));
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(exception.isCausedByNetworkIssue());
        
        exception = new TwitterException(new IOException());
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        assertFalse(exception.exceededRateLimitation());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        assertFalse(exception.exceededRateLimitation());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        assertTrue(exception.exceededRateLimitation());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        assertFalse(exception.resourceNotFound());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(404, null));
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        exception = new TwitterException("Test exception");
        assertEquals("", exception.getExceptionCode());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        assertEquals("", exception.getExceptionCode());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        assertEquals("", exception.getExceptionCode());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        assertEquals("", exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception");
        TwitterException exception2 = new TwitterException("Test exception");
        assertEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        assertEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        assertEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        assertEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception");
        exception2 = new TwitterException("Different exception");
        assertNotEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(404, null));
        assertNotEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        assertNotEquals(exception1, exception2);
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        assertNotEquals(exception1, exception2);
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception");
        TwitterException exception2 = new TwitterException("Test exception");
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception");
        exception2 = new TwitterException("Different exception");
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(404, null));
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, null));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        exception2 = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expected, exception.toString());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(420, null));
        expected = "TwitterException{exceptionCode=[], statusCode=420, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expected, exception.toString());
        
        exception = new TwitterException("Test exception", new HttpResponseImpl(400, new RateLimitStatusJSONImpl()));
        expected = "TwitterException{exceptionCode=[], statusCode=400, retryAfter=0, rateLimitStatus=RateLimitStatusJSONImpl{remainingHits=0, limit=0, resetTimeInSeconds=0, secondsUntilReset=0}, version=null}";
        assertEquals(expected, exception.toString());
    }
}