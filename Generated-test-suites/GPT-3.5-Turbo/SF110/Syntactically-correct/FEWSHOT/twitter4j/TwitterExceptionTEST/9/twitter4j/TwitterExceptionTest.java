package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TwitterExceptionTest {
    @Test
    public void testTwitterExceptionStringConstructor() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testTwitterExceptionExceptionConstructor() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause.toString(), exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testTwitterExceptionStringHttpResponseConstructor() {
        HttpResponse response = new HttpResponse("Test Response", 200);
        TwitterException exception = new TwitterException("Test Message", response);
        assertEquals("Test Response\nTest Message", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testTwitterExceptionStringExceptionConstructor() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Message", cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }
    
    @Test
    public void testTwitterExceptionStringExceptionIntConstructor() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Message", cause, 400);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("200000:TwitterException", exception.getExceptionCode());
    }
    
    @Test
    public void testGetStatusCode() {
        HttpResponse response = new HttpResponse("Test Response", 200);
        TwitterException exception = new TwitterException("Test Message", response);
        assertEquals(200, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        HttpResponse response = new HttpResponse("Test Response", 200);
        TwitterException exception = new TwitterException("Test Message", response);
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse response = new HttpResponse("Test Response", 200);
        TwitterException exception = new TwitterException("Test Message", response);
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        HttpResponse response = new HttpResponse("Test Response", 420);
        TwitterException exception = new TwitterException("Test Message", response);
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.isCausedByNetworkIssue());
        
        Exception cause = new IOException();
        exception = new TwitterException(cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.exceededRateLimitation());
        
        HttpResponse response = new HttpResponse("Test Response", 400);
        exception = new TwitterException("Test Message", response);
        assertTrue(exception.exceededRateLimitation());
        
        response = new HttpResponse("Test Response", 420);
        exception = new TwitterException("Test Message", response);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse response = new HttpResponse("Test Response", 404);
        exception = new TwitterException("Test Message", response);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        HttpResponse response = new HttpResponse("Test Response", 200);
        TwitterException exception = new TwitterException("Test Message", response);
        assertNull(exception.getExceptionCode());
        
        response = new HttpResponse("Test Response", 400);
        exception = new TwitterException("Test Message", response);
        assertEquals("200000:TwitterException", exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.nested);
        
        exception.setNested();
        assertTrue(exception.nested);
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Message");
        TwitterException exception2 = new TwitterException("Test Message");
        assertEquals(exception1, exception2);
        
        HttpResponse response = new HttpResponse("Test Response", 200);
        exception1 = new TwitterException("Test Message", response);
        exception2 = new TwitterException("Test Message", response);
        assertEquals(exception1, exception2);
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals(exception.hashCode(), exception.hashCode());
        
        HttpResponse response = new HttpResponse("Test Response", 200);
        exception = new TwitterException("Test Message", response);
        assertEquals(exception.hashCode(), exception.hashCode());
    }
    
    @Test
    public void testToString() {
        HttpResponse response = new HttpResponse("Test Response", 200);
        TwitterException exception = new TwitterException("Test Message", response);
        assertTrue(exception.toString().contains("Test Message"));
        assertTrue(exception.toString().contains("statusCode=200"));
        assertTrue(exception.toString().contains("retryAfter=0"));
        assertTrue(exception.toString().contains("rateLimitStatus=null"));
        assertTrue(exception.toString().contains("version=Test Version"));
    }
}