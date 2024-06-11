package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TwitterExceptionTest {
    @Test
    public void testTwitterException() {
        TwitterException exception = new TwitterException("message");
        assertEquals("message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause.getMessage(), exception.getCause().getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionWithMessageAndResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        response.addResponseHeader("Retry-After", "120");
        
        TwitterException exception = new TwitterException("message", response);
        assertEquals("200:There was no new data to return.\nmessage", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertEquals(120, exception.getRetryAfter());
    }
    
    @Test
    public void testTwitterExceptionWithMessageAndCause() {
        Exception cause = new Exception("cause");
        TwitterException exception = new TwitterException("message", cause);
        assertEquals("message", exception.getMessage());
        assertEquals(cause.getMessage(), exception.getCause().getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testTwiterExceptionWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("cause");
        TwitterException exception = new TwitterException("message", cause, 500);
        assertEquals("message", exception.getMessage());
        assertEquals(cause.getMessage(), exception.getCause().getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("message");
        assertEquals(-1, exception.getStatusCode());
        
        exception = new TwitterException("message", new HttpResponse());
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus status = new RateLimitStatusJSONImpl();
        
        TwitterException exception = new TwitterException("message");
        assertNull(exception.getRateLimitStatus());
        
        exception = new TwitterException("message", new HttpResponse());
        assertNull(exception.getRateLimitStatus());
        
        exception = new TwitterException("message", new HttpResponse());
        exception.rateLimitStatus = status;
        assertEquals(status, exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus status = new RateLimitStatusJSONImpl();
        
        TwitterException exception = new TwitterException("message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        
        exception = new TwitterException("message", new HttpResponse());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        
        exception = new TwitterException("message", new HttpResponse());
        exception.featureSpecificRateLimitStatus = status;
        assertEquals(status, exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("message");
        exception.getRetryAfter();
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        TwitterException exception = new TwitterException("message", response);
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("message");
        assertFalse(exception.isCausedByNetworkIssue());
        
        exception = new TwitterException(new IOException());
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("message");
        assertFalse(exception.exceededRateLimitation());
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        TwitterException exception = new TwitterException("message", response);
        assertTrue(exception.exceededRateLimitation());
        
        response.setStatusCode(200);
        exception = new TwitterException("message", response);
        assertFalse(exception.exceededRateLimitation());
        
        response.setStatusCode(420);
        exception = new TwitterException("message", response);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("message");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse response = new HttpResponse();
        response.setStatusCode(404);
        TwitterException exception = new TwitterException("message", response);
        assertTrue(exception.resourceNotFound());
        
        response.setStatusCode(200);
        exception = new TwitterException("message", response);
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("message");
        assertEquals("", exception.getExceptionCode());
        
        exception = new TwitterException("message", new HttpResponse());
        assertEquals("", exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("message");
        assertFalse(exception.nested);
        
        exception.setNested();
        assertTrue(exception.nested);
    }
    
    @Test
    public void testEquals() {
        RateLimitStatus status1 = new RateLimitStatusJSONImpl();
        RateLimitStatus status2 = new RateLimitStatusJSONImpl();
        
        TwitterException exception1 = new TwitterException("message");
        TwitterException exception2 = new TwitterException("message");
        assertTrue(exception1.equals(exception2));
        
        exception1 = new TwitterException("message", new HttpResponse());
        exception2 = new TwitterException("message", new HttpResponse());
        assertTrue(exception1.equals(exception2));
        
        exception1.rateLimitStatus = status1;
        assertFalse(exception1.equals(exception2));
        exception2.rateLimitStatus = status2;
        assertFalse(exception1.equals(exception2));
        exception2.rateLimitStatus = status1;
        assertTrue(exception1.equals(exception2));
        
        exception1.retryAfter = 120;
        assertFalse(exception1.equals(exception2));
        exception2.retryAfter = 120;
        assertTrue(exception1.equals(exception2));
        
        exception1.statusCode = 500;
        assertFalse(exception1.equals(exception2));
        exception2.statusCode = 500;
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        RateLimitStatus status = new RateLimitStatusJSONImpl();
        
        TwitterException exception = new TwitterException("message");
        assertEquals(0, exception.hashCode());
        
        exception.rateLimitStatus = status;
        assertEquals(status.hashCode(), exception.hashCode());
    }
    
    @Test
    public void testToString() {
        RateLimitStatus status = new RateLimitStatusJSONImpl();
        
        TwitterException exception = new TwitterException("message");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=VERSION}", exception.toString());
        
        exception.nested = true;
        assertEquals("TwitterException{statusCode=-1, retryAfter=0, rateLimitStatus=null, version=VERSION}", exception.toString());
        
        exception.rateLimitStatus = status;
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=" + status + ", version=VERSION}", exception.toString());
    }
    
    @Test
    public void testGetCause() {
        HttpResponse response = new HttpResponse();
        
        String cause = TwitterException.getCause(response);
        assertEquals("204:There was no new data to return.", cause);
        
        response.setStatusCode(400);
        cause = TwitterException.getCause(response);
        assertEquals("400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", cause);
    }
}