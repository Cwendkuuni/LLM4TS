import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithStringParam() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("Test message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testConstructorWithExceptionParam() {
        Exception cause = new Exception("Test exception");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testConstructorWithHttpResParam() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        res.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test message", res);
        assertEquals("Test message", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testConstructorWithExceptionAndStatusParams() {
        Exception cause = new Exception("Test exception");
        TwitterException exception = new TwitterException("Test message", cause, 500);
        assertEquals("Test message", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test message", res);
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.isCausedByNetworkIssue());

        Exception cause = new IOException();
        exception = new TwitterException("Test message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.exceededRateLimitation());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(400);
        exception = new TwitterException("Test message", res);
        assertTrue(exception.exceededRateLimitation());

        res.setResponseHeader("Retry-After", "60");
        exception = new TwitterException("Test message", res);
        assertTrue(exception.exceededRateLimitation());

        res.setStatusCode(200);
        exception = new TwitterException("Test message", res);
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.resourceNotFound());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(404);
        exception = new TwitterException("Test message", res);
        assertTrue(exception.resourceNotFound());

        res.setStatusCode(200);
        exception = new TwitterException("Test message", res);
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("", exception.getExceptionCode());

        exception = new TwitterException("Test message", new Exception("Test exception"));
        assertTrue(exception.getExceptionCode().startsWith("java.lang.Exception:"));

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(404);
        exception = new TwitterException("Test message", res);
        assertTrue(exception.getExceptionCode().endsWith("404:The URI requested is invalid or the resource requested, such as a user, does not exists."));
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.nested);
        
        exception.setNested();
        assertTrue(exception.nested);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message");
        TwitterException exception2 = new TwitterException("Test message");
        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception2));

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        TwitterException exception3 = new TwitterException("Test message", res);
        assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test message");
        TwitterException exception2 = new TwitterException("Test message");
        assertEquals(exception1.hashCode(), exception2.hashCode());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        TwitterException exception3 = new TwitterException("Test message", res);
        assertNotEquals(exception1.hashCode(), exception3.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        assertTrue(exception.toString().contains("TwitterException"));
        assertFalse(exception.toString().contains("exceptionCode"));
        assertTrue(exception.toString().contains("statusCode=-1"));

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        TwitterException exception2 = new TwitterException("Test message", res);
        assertTrue(exception2.toString().contains("exceptionCode=[HTTP status code: 200],"));
        assertTrue(exception2.toString().contains("statusCode=200"));
    }
}