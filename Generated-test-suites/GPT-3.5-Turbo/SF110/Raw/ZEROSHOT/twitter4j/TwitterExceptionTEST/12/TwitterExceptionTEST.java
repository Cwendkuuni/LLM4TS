package twitter4j;

import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testConstructorWithMessageAndHttpResponse() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        TwitterException exception = new TwitterException("Test Exception", res);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception", cause, 500);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeNot420() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.getRetryAfter();
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test Exception", new HttpResponseImpl());
        assertFalse(exception1.exceededRateLimitation());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(400);
        TwitterException exception2 = new TwitterException("Test Exception", res);
        assertTrue(exception2.exceededRateLimitation());
        
        HttpResponse res2 = new HttpResponseImpl();
        res2.setStatusCode(420);
        TwitterException exception3 = new TwitterException("Test Exception", res2);
        assertTrue(exception3.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception1 = new TwitterException("Test Exception");
        assertFalse(exception1.resourceNotFound());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(404);
        TwitterException exception2 = new TwitterException("Test Exception", res);
        assertTrue(exception2.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("", exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        assertTrue(exception1.equals(exception1));
        
        TwitterException exception2 = new TwitterException("Test Exception");
        assertTrue(exception1.equals(exception2));
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        exception1 = new TwitterException("Test Exception", res);
        exception2 = new TwitterException("Test Exception", res);
        assertTrue(exception1.equals(exception2));
        
        exception2 = new TwitterException("Test Exception", new HttpResponseImpl());
        assertFalse(exception1.equals(exception2));
        
        exception2 = new TwitterException("Test Exception");
        assertFalse(exception1.equals(exception2));
        
        res.setStatusCode(400);
        exception2 = new TwitterException("Test Exception", res);
        assertFalse(exception1.equals(exception2));
        
        exception2 = new TwitterException("Test Exception");
        assertFalse(exception1.equals(exception2));
        
        exception2 = new TwitterException("Test Exception", new Exception("Test Cause"));
        assertFalse(exception1.equals(exception2));
        
        exception2 = new TwitterException("Test Exception");
        assertFalse(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception");
        assertTrue(exception1.hashCode() == exception1.hashCode());
        
        TwitterException exception2 = new TwitterException("Test Exception");
        assertTrue(exception1.hashCode() == exception2.hashCode());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        exception1 = new TwitterException("Test Exception", res);
        exception2 = new TwitterException("Test Exception", res);
        assertTrue(exception1.hashCode() == exception2.hashCode());
        
        exception2 = new TwitterException("Test Exception", new HttpResponseImpl());
        assertFalse(exception1.hashCode() == exception2.hashCode());
        
        exception2 = new TwitterException("Test Exception");
        assertFalse(exception1.hashCode() == exception2.hashCode());
        
        res.setStatusCode(400);
        exception2 = new TwitterException("Test Exception", res);
        assertFalse(exception1.hashCode() == exception2.hashCode());
        
        exception2 = new TwitterException("Test Exception");
        assertFalse(exception1.hashCode() == exception2.hashCode());
        
        exception2 = new TwitterException("Test Exception", new Exception("Test Cause"));
        assertFalse(exception1.hashCode() == exception2.hashCode());
        
        exception2 = new TwitterException("Test Exception");
        assertFalse(exception1.hashCode() == exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=}", exception.toString());
        
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        exception = new TwitterException("Test Exception", res);
        assertEquals("TwitterException{statusCode=420, retryAfter=0, rateLimitStatus=null, version=}", exception.toString());
        
        exception.setNested();
        assertEquals("TwitterException{exceptionCode=[], statusCode=420, retryAfter=0, rateLimitStatus=null, version=}", exception.toString());
    }
}