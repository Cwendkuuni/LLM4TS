package twitter4j;

import twitter4j.internal.http.*;
import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterException1() {
        try {
            TwitterException exception = new TwitterException("Test message");
            assertEquals("Test message", exception.getMessage());
            assertEquals(-1, exception.getStatusCode());
            assertNull(exception.getRateLimitStatus());
            assertNull(exception.getFeatureSpecificRateLimitStatus());
            assertFalse(exception.isCausedByNetworkIssue());
            assertFalse(exception.exceededRateLimitation());
            assertFalse(exception.resourceNotFound());
            assertNull(exception.getExceptionCode());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testTwitterException2() {
        try {
            TwitterException cause = new TwitterException("Test cause");
            TwitterException exception = new TwitterException(cause);
            
            assertEquals(cause, exception.getCause());
            assertEquals(-1, exception.getStatusCode());
            assertNull(exception.getRateLimitStatus());
            assertNull(exception.getFeatureSpecificRateLimitStatus());
            assertFalse(exception.isCausedByNetworkIssue());
            assertFalse(exception.exceededRateLimitation());
            assertFalse(exception.resourceNotFound());
            assertNull(exception.getExceptionCode());
            
            assertEquals(true, cause.nested);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testTwitterException3() {
        try {
            HttpResponse res = new HttpResponseImpl();
            res.setStatusCode(420);
            res.setResponseHeader("Retry-After", "60");
            
            TwitterException exception = new TwitterException("Test message", res);
            
            assertEquals("Test message", exception.getMessage());
            assertEquals(420, exception.getStatusCode());
            assertNotNull(exception.getRateLimitStatus());
            assertNotNull(exception.getFeatureSpecificRateLimitStatus());
            assertFalse(exception.isCausedByNetworkIssue());
            assertFalse(exception.exceededRateLimitation());
            assertFalse(exception.resourceNotFound());
            assertNotNull(exception.getExceptionCode());
            assertEquals(60, exception.getRetryAfter());
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testTwitterException4() {
        try {
            Exception cause = new Exception("Test cause");
            
            TwitterException exception = new TwitterException("Test message", cause);
            
            assertEquals("Test message", exception.getMessage());
            assertEquals(-1, exception.getStatusCode());
            assertNull(exception.getRateLimitStatus());
            assertNull(exception.getFeatureSpecificRateLimitStatus());
            assertFalse(exception.isCausedByNetworkIssue());
            assertFalse(exception.exceededRateLimitation());
            assertFalse(exception.resourceNotFound());
            assertNull(exception.getExceptionCode());
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testTwitterException5() {
        try {
            Exception cause = new Exception("Test cause");
            
            TwitterException exception = new TwitterException("Test message", cause, 500);
            
            assertEquals("Test message", exception.getMessage());
            assertEquals(500, exception.getStatusCode());
            assertNull(exception.getRateLimitStatus());
            assertNull(exception.getFeatureSpecificRateLimitStatus());
            assertFalse(exception.isCausedByNetworkIssue());
            assertFalse(exception.exceededRateLimitation());
            assertFalse(exception.resourceNotFound());
            assertNull(exception.getExceptionCode());
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test message", new HttpResponseImpl());
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test message", new HttpResponseImpl());
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message", new IOException());
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test message", new HttpResponseImpl(400));
        assertTrue(exception1.exceededRateLimitation());
        
        TwitterException exception2 = new TwitterException("Test message", new HttpResponseImpl(420));
        assertTrue(exception2.exceededRateLimitation());
        
        TwitterException exception3 = new TwitterException("Test message", new HttpResponseImpl(500));
        assertFalse(exception3.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception1 = new TwitterException("Test message", new HttpResponseImpl(404));
        assertTrue(exception1.resourceNotFound());
        
        TwitterException exception2 = new TwitterException("Test message", new HttpResponseImpl(200));
        assertFalse(exception2.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message", new HttpResponseImpl(500));
        assertNotNull(exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test message");
        exception.setNested();
        assertTrue(exception.nested);
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message", new HttpResponseImpl(500));
        TwitterException exception2 = new TwitterException("Test message", new HttpResponseImpl(500));
        assertTrue(exception1.equals(exception2));
        
        TwitterException exception3 = new TwitterException("Test message", new HttpResponseImpl(400));
        assertFalse(exception1.equals(exception3));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test message", new HttpResponseImpl(500));
        TwitterException exception2 = new TwitterException("Test message", new HttpResponseImpl(500));
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message", new HttpResponseImpl(500));
        assertNotNull(exception.toString());
    }

}