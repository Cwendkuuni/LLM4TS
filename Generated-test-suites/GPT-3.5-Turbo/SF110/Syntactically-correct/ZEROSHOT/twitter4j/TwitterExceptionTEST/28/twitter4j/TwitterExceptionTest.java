import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testTwitterExceptionMsgConstructor() {
        String msg = "Test exception message";
        TwitterException exception = new TwitterException(msg);
        assertEquals(msg, exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testTwitterExceptionCauseConstructor() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause.toString(), exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testTwitterExceptionMsgResConstructor() {
        String msg = "Test exception message";
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        TwitterException exception = new TwitterException(msg, res);
        assertEquals(msg, exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testTwitterExceptionMsgCauseConstructor() {
        String msg = "Test exception message";
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testTwitterExceptionMsgCauseStatusCodeConstructor() {
        String msg = "Test exception message";
        Exception cause = new Exception("Test cause");
        int statusCode = 200;
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test");
        assertEquals(-1, exception.getStatusCode());
        exception = new TwitterException("Test", new HttpResponseImpl());
        assertEquals(200, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test");
        assertNull(exception.getRateLimitStatus());
        exception = new TwitterException("Test", new HttpResponseImpl());
        assertNull(exception.getRateLimitStatus());
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        ((HttpResponseImpl)exception).setResponseHeader("x-rate-limit-limit", "100");
        ((HttpResponseImpl)exception).setResponseHeader("x-rate-limit-remaining", "50");
        ((HttpResponseImpl)exception).setResponseHeader("x-rate-limit-reset", "1555645199");
        ((HttpResponseImpl)exception).setResponseHeader("x-feature-rate-limit-limit", "100");
        ((HttpResponseImpl)exception).setResponseHeader("x-feature-rate-limit-remaining", "20");
        ((HttpResponseImpl)exception).setResponseHeader("x-feature-rate-limit-reset", "1555645899");
        exception = new TwitterException("Test", new HttpResponseImpl());
        rateLimitStatus.setLimit(100);
        rateLimitStatus.setRemaining(50);
        rateLimitStatus.setResetTimeInSeconds(1555645199);
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        exception = new TwitterException("Test", new HttpResponseImpl());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl();
        ((HttpResponseImpl)exception).setResponseHeader("x-feature-rate-limit-limit", "100");
        ((HttpResponseImpl)exception).setResponseHeader("x-feature-rate-limit-remaining", "20");
        ((HttpResponseImpl)exception).setResponseHeader("x-feature-rate-limit-reset", "1555645899");
        exception = new TwitterException("Test", new HttpResponseImpl());
        featureSpecificRateLimitStatus.setLimit(100);
        featureSpecificRateLimitStatus.setRemaining(20);
        featureSpecificRateLimitStatus.setResetTimeInSeconds(1555645899);
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test");
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        exception = new TwitterException("Test", res);
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test");
        assertFalse(exception.isCausedByNetworkIssue());
        exception = new TwitterException(new IOException("Network issue"));
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test");
        assertFalse(exception.exceededRateLimitation());
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(400);
        exception = new TwitterException("Test", res);
        assertTrue(exception.exceededRateLimitation());
        res = new HttpResponseImpl();
        res.setStatusCode(420);
        exception = new TwitterException("Test", res);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test");
        assertFalse(exception.resourceNotFound());
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(404);
        exception = new TwitterException("Test", res);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test");
        assertEquals("", exception.getExceptionCode());
        exception = new TwitterException("Test", new HttpResponseImpl());
        assertEquals("200:", exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test");
        assertFalse(exception.nested);
        exception.setNested();
        assertTrue(exception.nested);
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test");
        TwitterException exception2 = new TwitterException("Test");
        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals("Test"));
        exception2.setNested();
        assertFalse(exception1.equals(exception2));
        exception2.setNested();
        exception2.statusCode = 200;
        assertFalse(exception1.equals(exception2));
        exception1.statusCode = 200;
        assertTrue(exception1.equals(exception2));
        exception2.retryAfter = 60;
        assertFalse(exception1.equals(exception2));
        exception1.retryAfter = 60;
        assertTrue(exception1.equals(exception2));
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        exception2.rateLimitStatus = rateLimitStatus;
        assertFalse(exception1.equals(exception2));
        exception1.rateLimitStatus = rateLimitStatus;
        assertTrue(exception1.equals(exception2));
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl();
        exception2.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertFalse(exception1.equals(exception2));
        exception1.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test");
        assertEquals(0, exception.hashCode());
        exception.statusCode = 200;
        assertEquals(200, exception.hashCode());
        exception.retryAfter = 60;
        assertNotNull(exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test");
        assertFalse(exception.toString().contains("exceptionCode"));
        exception.setNested();
        assertTrue(exception.toString().contains("exceptionCode"));
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        exception = new TwitterException("Test", res);
        assertTrue(exception.toString().contains("statusCode="));
        assertTrue(exception.toString().contains("retryAfter="));
        assertTrue(exception.toString().contains("rateLimitStatus="));
        assertTrue(exception.toString().contains("version="));
    }
}