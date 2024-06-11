import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TwitterExceptionTest {
    
    @Test
    public void testTwitterExceptionConstructorWithMessage() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertEquals(Version.getVersion(), exception.toString());
    }
    
    @Test
    public void testTwitterExceptionConstructorWithCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertEquals(Version.getVersion(), exception.toString());
    }
    
    @Test
    public void testTwitterExceptionConstructorWithMessageAndResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        
        TwitterException exception = new TwitterException("Test Message", response);
        assertEquals(getCause(response) + "\n" + "Test Message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(Version.getVersion(), exception.toString());
    }
    
    @Test
    public void testTwitterExceptionConstructorWithMessageAndCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Message", cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertEquals(Version.getVersion(), exception.toString());
    }
    
    @Test
    public void testTwitterExceptionConstructorWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Message", cause, 500);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertEquals(Version.getVersion(), exception.toString());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        HttpResponse response = new HttpResponse();
        response.setResponseHeader("x-rate-limit-limit", "100");
        response.setResponseHeader("x-rate-limit-remaining", "50");
        response.setResponseHeader("x-rate-limit-reset", "1628628564");

        TwitterException exception = new TwitterException("Test Message", response);
        assertEquals("100", exception.getRateLimitStatus().getLimit());
        assertEquals("50", exception.getRateLimitStatus().getRemaining());
        assertEquals("1628628564", exception.getRateLimitStatus().getResetTimeInSeconds());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse response = new HttpResponse();
        response.setResponseHeader("x-feature-specific-rate-limit-limit", "100");
        response.setResponseHeader("x-feature-specific-rate-limit-remaining", "50");
        response.setResponseHeader("x-feature-specific-rate-limit-reset", "1628628564");

        TwitterException exception = new TwitterException("Test Message", response);
        assertEquals("100", exception.getFeatureSpecificRateLimitStatus().getLimit());
        assertEquals("50", exception.getFeatureSpecificRateLimitStatus().getRemaining());
        assertEquals("1628628564", exception.getFeatureSpecificRateLimitStatus().getResetTimeInSeconds());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        HttpResponse response = new HttpResponse();
        TwitterException exception = new TwitterException("Test Message", response);
        
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException(new IOException("Test IOException"));
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);

        TwitterException exception = new TwitterException("Test Message", response);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(404);

        TwitterException exception = new TwitterException("Test Message", response);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("", exception.getExceptionCode());
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
        assertTrue(exception1.equals(exception2));
        
        HttpResponse response = new HttpResponse();
        response.setResponseHeader("x-rate-limit-limit", "100");
        exception1 = new TwitterException("Test Message", response);
        exception2 = new TwitterException("Test Message", response);
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Message");
        TwitterException exception2 = new TwitterException("Test Message");
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        HttpResponse response = new HttpResponse();
        response.setResponseHeader("x-rate-limit-limit", "100");
        exception1 = new TwitterException("Test Message", response);
        exception2 = new TwitterException("Test Message", response);
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    private String getCause(HttpResponse res) {
        //... code from TwitterException class
    }
}