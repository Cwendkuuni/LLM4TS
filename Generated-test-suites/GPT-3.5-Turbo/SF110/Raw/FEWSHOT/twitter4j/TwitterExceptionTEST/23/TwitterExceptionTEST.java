import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TwitterExceptionTest {
  
    @Test
    public void testConstructorWithMessage() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals("Test message", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getCause());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("", twitterException.getExceptionCode());
    }
    
    @Test
    public void testConstructorWithCause() {
        TwitterException causeException = new TwitterException("Exception cause");
        TwitterException twitterException = new TwitterException(causeException);
        assertEquals(causeException.toString(), twitterException.getCause().toString());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertTrue(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("", twitterException.getExceptionCode());
    }
    
    @Test
    public void testConstructorWithMessageAndResponse() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "120");
        TwitterException twitterException = new TwitterException("Test message", res);
        assertEquals("Test message", twitterException.getMessage());
        assertEquals(420, twitterException.getStatusCode());
        assertEquals(120, twitterException.getRetryAfter());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertTrue(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("", twitterException.getExceptionCode());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        TwitterException causeException = new TwitterException("Exception cause");
        TwitterException twitterException = new TwitterException("Test message", causeException);
        assertEquals("Test message", twitterException.getMessage());
        assertEquals(causeException.toString(), twitterException.getCause().toString());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("", twitterException.getExceptionCode());
    }
    
    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        TwitterException causeException = new TwitterException("Exception cause");
        TwitterException twitterException = new TwitterException("Test message", causeException, 500);
        assertEquals("Test message", twitterException.getMessage());
        assertEquals(causeException.toString(), twitterException.getCause().toString());
        assertEquals(500, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("", twitterException.getExceptionCode());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals(-1, twitterException.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "120");
        TwitterException twitterException = new TwitterException("Test message", res);
        assertEquals(120, twitterException.getRetryAfter());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeNot420() {
        TwitterException twitterException = new TwitterException("Test message");
        twitterException.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException twitterException = new TwitterException("Test message");
        assertFalse(twitterException.isCausedByNetworkIssue());
        
        twitterException = new TwitterException(new IOException("Network issue"));
        assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException = new TwitterException("Test message");
        assertFalse(twitterException.exceededRateLimitation());
        
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        res.setResponseHeader("X-Rate-Limit-Reset", "1577712000");
        twitterException = new TwitterException("Test message", res);
        assertTrue(twitterException.exceededRateLimitation());
        
        res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "120");
        twitterException = new TwitterException("Test message", res);
        assertTrue(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test message");
        assertFalse(twitterException.resourceNotFound());
        
        HttpResponse res = new HttpResponse();
        res.setStatusCode(404);
        twitterException = new TwitterException("Test message", res);
        assertTrue(twitterException.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals("", twitterException.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException twitterException = new TwitterException("Test message");
        assertFalse(twitterException.toString().contains("exceptionCode=["));
        twitterException.setNested();
        assertTrue(twitterException.toString().contains("exceptionCode=["));
    }
    
    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test message");
        TwitterException twitterException2 = new TwitterException("Test message");
        assertEquals(twitterException1, twitterException2);
        
        twitterException1 = new TwitterException("Test message", new IOException("Network issue"));
        twitterException2 = new TwitterException("Test message", new IOException("Network issue"));
        assertEquals(twitterException1, twitterException2);
        
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        res.setResponseHeader("X-Rate-Limit-Reset", "1577712000");
        twitterException1 = new TwitterException("Test message", res);
        twitterException2 = new TwitterException("Test message", res);
        assertEquals(twitterException1, twitterException2);
    }
    
    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals(2128546323, twitterException.hashCode());
        
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "120");
        twitterException = new TwitterException("Test message", res);
        assertEquals(2088240154, twitterException.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals("TwitterException{statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", twitterException.toString());
        
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "120");
        twitterException = new TwitterException("Test message", res);
        assertEquals("TwitterException{statusCode=420, retryAfter=120, rateLimitStatus=null, version=null}", twitterException.toString());
        
        twitterException.setNested();
        assertEquals("TwitterException{exceptionCode=[], statusCode=420, retryAfter=120, rateLimitStatus=null, version=null}", twitterException.toString());
    }
}