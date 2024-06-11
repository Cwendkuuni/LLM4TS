package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TwitterExceptionTest {
    
    private TwitterException exception;
    private HttpResponse response;
    
    @Before
    public void setUp() throws IOException {
        response = new HttpResponse();
    }
    
    @Test
    public void testConstructor1() {
        exception = new TwitterException("message");
        assertEquals("message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertFalse(exception.resourceNotFound());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.isCausedByNetworkIssue());
        assertNull(exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.equals(null));
    }
    
    @Test
    public void testConstructor2() {
        Exception cause = new IOException();
        exception = new TwitterException(cause);
        assertEquals(cause.toString(), exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertFalse(exception.resourceNotFound());
        assertFalse(exception.exceededRateLimitation());
        assertTrue(exception.isCausedByNetworkIssue());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.equals(null));
    }
    
    @Test
    public void testConstructor3() {
        response.setStatusCode(200);
        response.setResponseHeader("Retry-After", "10");
        exception = new TwitterException("message", response);
        assertEquals("200:", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertFalse(exception.resourceNotFound());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.isCausedByNetworkIssue());
        assertNull(exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals("twitter4j", exception.getExceptionCode());
        assertFalse(exception.equals(null));
    }
    
    @Test
    public void testConstructor4() {
        Exception cause = new IOException();
        exception = new TwitterException("message", cause);
        assertEquals("message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertFalse(exception.resourceNotFound());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.isCausedByNetworkIssue());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.equals(null));
    }
    
    @Test
    public void testConstructor5() {
        Exception cause = new IOException();
        exception = new TwitterException("message", cause, 200);
        assertEquals("message", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertFalse(exception.resourceNotFound());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.isCausedByNetworkIssue());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.equals(null));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        exception = new TwitterException("message", response);
        exception.getRetryAfter();
    }
    
    @Test
    public void testResourceNotFound() {
        exception = new TwitterException("message", response);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testExceededRateLimitation() {
        response.setStatusCode(400);
        response.setResponseHeader("Retry-After", "10");
        exception = new TwitterException("message", response);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testEquals() {
        exception = new TwitterException("message", response);
        
        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(null));
        
        TwitterException exception2 = new TwitterException("message", response);
        assertTrue(exception.equals(exception2));
        
        TwitterException exception3 = new TwitterException("message", response);
        exception3.setNested();
        assertFalse(exception.equals(exception3));
        
        HttpResponse response2 = new HttpResponse();
        response2.setStatusCode(200);
        response2.setResponseHeader("Retry-After", "10");
        TwitterException exception4 = new TwitterException("message", response2); 
        assertFalse(exception.equals(exception4));
    }
    
    @Test
    public void testHashCode() {
        exception = new TwitterException("message", response);
        
        TwitterException exception2 = new TwitterException("message", response);
        assertEquals(exception.hashCode(), exception2.hashCode());
        
        HttpResponse response2 = new HttpResponse();
        response2.setStatusCode(200);
        response2.setResponseHeader("Retry-After", "10");
        TwitterException exception3 = new TwitterException("message", response2); 
        assertNotEquals(exception.hashCode(), exception3.hashCode());
    }
    
    @Test
    public void testToString() {
        exception = new TwitterException("message", response);
        String expectedToString = "TwitterException{exceptionCode=[twitter4j], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expectedToString, exception.toString());
    }
}