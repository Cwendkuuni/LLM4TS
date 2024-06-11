import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testTwitterExceptionMsg() {
        TwitterException exception = new TwitterException("This is an error message");
        assertNotNull(exception);
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getCause());
        assertEquals("This is an error message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionCause() {
        Exception cause = new Exception("This is the cause");
        TwitterException exception = new TwitterException(cause);
        assertNotNull(exception);
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getMessage());
    }

    @Test
    public void testTwitterExceptionMsgRes() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(404);
        res.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("This is an error message", res);
        assertNotNull(exception);
        assertEquals(404, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertTrue(exception.resourceNotFound());
        assertNull(exception.getCause());
        assertEquals("404:The URI requested is invalid or the resource requested, such as a user, does not exists.\nThis is an error message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionMsgAndCause() {
        Exception cause = new Exception("This is the cause");
        TwitterException exception = new TwitterException("This is an error message", cause);
        assertNotNull(exception);
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(cause, exception.getCause());
        assertEquals("This is an error message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionMsgCauseAndStatusCode() {
        Exception cause = new Exception("This is the cause");
        TwitterException exception = new TwitterException("This is an error message", cause, 500);
        assertNotNull(exception);
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(cause, exception.getCause());
        assertEquals("This is an error message", exception.getMessage());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("This is an error message");
        assertEquals(-1, exception.getStatusCode());
        HttpResponse res = new HttpResponse();
        res.setStatusCode(500);
        exception = new TwitterException("This is another error message", res);
        assertEquals(500, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("This is an error message");
        assertNull(exception.getRateLimitStatus());
        HttpResponse res = new HttpResponse();
        res.setStatusCode(500);
        exception = new TwitterException("This is another error message", res);
        assertNotNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("This is an error message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        HttpResponse res = new HttpResponse();
        res.setStatusCode(500);
        exception = new TwitterException("This is another error message", res);
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("This is an error message");
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception caught, test passed
        }
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        exception = new TwitterException("This is another error message", res);
        assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("This is an error message");
        assertFalse(exception.isCausedByNetworkIssue());
        exception = new TwitterException(new IOException("network issue"));
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("This is an error message");
        assertFalse(exception.exceededRateLimitation());
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        res.setResponseHeader("Retry-After", "60");
        exception = new TwitterException("This is another error message", res);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("This is an error message");
        assertFalse(exception.resourceNotFound());
        HttpResponse res = new HttpResponse();
        res.setStatusCode(404);
        res.setResponseHeader("Retry-After", "60");
        exception = new TwitterException("This is another error message", res);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("This is an error message");
        String exceptionCode = exception.getExceptionCode();
        assertNotNull(exceptionCode);
        assertTrue(exceptionCode.contains("@b2e1"));
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("This is an error message");
        TwitterException exception2 = new TwitterException("This is an error message");
        assertTrue(exception1.equals(exception2));

        HttpResponse res1 = new HttpResponse();
        res1.setStatusCode(500);
        TwitterException exception3 = new TwitterException("This is an error message", res1);
        HttpResponse res2 = new HttpResponse();
        res2.setStatusCode(500);
        TwitterException exception4 = new TwitterException("This is an error message", res2);
        assertTrue(exception3.equals(exception4));

        HttpResponse res3 = new HttpResponse();
        res3.setStatusCode(400);
        TwitterException exception5 = new TwitterException("This is an error message", res3);
        HttpResponse res4 = new HttpResponse();
        res4.setStatusCode(200);
        TwitterException exception6 = new TwitterException("This is an error message", res4);
        assertFalse(exception5.equals(exception6));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("This is an error message");
        TwitterException exception2 = new TwitterException("This is an error message");
        assertTrue(exception1.hashCode() == exception2.hashCode());

        HttpResponse res1 = new HttpResponse();
        res1.setStatusCode(500);
        TwitterException exception3 = new TwitterException("This is an error message", res1);
        HttpResponse res2 = new HttpResponse();
        res2.setStatusCode(500);
        TwitterException exception4 = new TwitterException("This is an error message", res2);
        assertTrue(exception3.hashCode() == exception4.hashCode());

        HttpResponse res3 = new HttpResponse();
        res3.setStatusCode(400);
        TwitterException exception5 = new TwitterException("This is an error message", res3);
        HttpResponse res4 = new HttpResponse();
        res4.setStatusCode(200);
        TwitterException exception6 = new TwitterException("This is an error message", res4);
        assertFalse(exception5.hashCode() == exception6.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("This is an error message");
        assertTrue(exception.toString().contains("statusCode=-1"));
        assertTrue(exception.toString().contains("retryAfter=0"));
        assertTrue(exception.toString().contains("rateLimitStatus=null"));
        assertTrue(exception.toString().contains("version="));

        HttpResponse res = new HttpResponse();
        res.setStatusCode(404);
        res.setResponseHeader("Retry-After", "60");
        exception = new TwitterException("This is another error message", res);
        assertTrue(exception.toString().contains("statusCode=404"));
        assertTrue(exception.toString().contains("retryAfter=60"));
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.toString().contains("rateLimitStatus="));
        assertTrue(exception.toString().contains("version="));
    }
}