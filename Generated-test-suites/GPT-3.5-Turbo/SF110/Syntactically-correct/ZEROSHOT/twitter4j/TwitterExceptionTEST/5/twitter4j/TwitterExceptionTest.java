import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.internal.http.HttpResponse;
import java.io.IOException;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructor1() {
        TwitterException exception = new TwitterException("Test");
        assertEquals("Test", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructor2() {
        IOException cause = new IOException();
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructor3() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        
        TwitterException exception = new TwitterException("Test", res);
        assertEquals("Test\n420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testConstructor4() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test", cause);
        assertEquals("Test", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructor5() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test", cause, 500);
        assertEquals("Test", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test");
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test");
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test");
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException();
        TwitterException exception = new TwitterException(cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test");
        assertFalse(exception1.exceededRateLimitation());
        
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        TwitterException exception2 = new TwitterException("Test", res);
        assertTrue(exception2.exceededRateLimitation());
        
        res.setStatusCode(420);
        TwitterException exception3 = new TwitterException("Test", res);
        assertTrue(exception3.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception1 = new TwitterException("Test");
        assertFalse(exception1.resourceNotFound());
        
        HttpResponse res = new HttpResponse();
        res.setStatusCode(404);
        TwitterException exception2 = new TwitterException("Test", res);
        assertTrue(exception2.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test");
        assertNotNull(exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test");
        TwitterException exception2 = new TwitterException("Test");
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test");
        assertNotNull(exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test");
        assertNotNull(exception.toString());
    }
    
    @Test
    public void testGetCause() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        String expectedCause = "420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
        String actualCause = TwitterExceptionTest.getCause(res);
        assertEquals(expectedCause, actualCause);
    }

}