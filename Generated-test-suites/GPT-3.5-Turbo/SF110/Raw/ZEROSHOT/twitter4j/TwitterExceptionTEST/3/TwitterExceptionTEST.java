import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    private TwitterException twitterException;
    
    @Before
    public void setUp() {
        twitterException = new TwitterException("Test Exception");
    }
    
    @Test
    public void testConstructorWithMessage() {
        assertEquals("Test Exception", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Cause Exception");
        twitterException = new TwitterException(cause);
        assertEquals("Cause Exception", twitterException.getCause().getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageAndResponse() {
        HttpResponse res = new HttpResponseImpl();
        twitterException = new TwitterException("Test Exception", res);
        assertEquals("Test Exception\n:null", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        Exception cause = new Exception("Cause Exception");
        twitterException = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception", twitterException.getMessage());
        assertEquals("Cause Exception", twitterException.getCause().getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("Cause Exception");
        twitterException = new TwitterException("Test Exception", cause, 500);
        assertEquals("Test Exception", twitterException.getMessage());
        assertEquals("Cause Exception", twitterException.getCause().getMessage());
        assertEquals(500, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
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
    public void testGetRetryAfterException() {
        twitterException.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(twitterException.isCausedByNetworkIssue());
        Exception cause = new IOException();
        twitterException = new TwitterException("Test Exception", cause);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        assertFalse(twitterException.exceededRateLimitation());
        twitterException = new TwitterException("Test Exception", new HttpResponseImpl());
        assertTrue(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        assertFalse(twitterException.resourceNotFound());
        twitterException = new TwitterException("Test Exception", new HttpResponseImpl(404, ""));
        assertTrue(twitterException.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        assertEquals("0", twitterException.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        assertTrue(exception1.equals(exception1));
        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals("Test Exception"));
        TwitterException exception2 = new TwitterException("Test Exception");
        assertTrue(exception1.equals(exception2));
        exception2.setNested();
        assertFalse(exception1.equals(exception2));
        HttpResponse res = new HttpResponseImpl(500, "");
        exception2 = new TwitterException("Test Exception", res);
        assertFalse(exception1.equals(exception2));
        exception1 = new TwitterException("Test Exception", res);
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", exception.toString());
    }
}