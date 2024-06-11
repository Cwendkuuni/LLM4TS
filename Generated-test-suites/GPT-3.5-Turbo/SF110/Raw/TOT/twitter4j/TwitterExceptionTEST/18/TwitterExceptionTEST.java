import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionConstructor1() {
        TwitterException exception = new TwitterException("Test exception message");
        assertNotNull(exception);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructor2() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(cause);
        assertNotNull(exception);
        assertEquals("Test cause", exception.getCause().getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructor3() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        
        TwitterException exception = new TwitterException("Test exception message", res);
        assertNotNull(exception);
        assertEquals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.\nTest exception message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertNotNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructor4() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test exception message", cause);
        assertNotNull(exception);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructor5() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test exception message", cause, 500);
        assertNotNull(exception);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test exception message");
        assertEquals(-1, exception.getStatusCode());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);

        exception = new TwitterException("Test exception message", res);
        assertEquals(200, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test exception message");
        assertNull(exception.getRateLimitStatus());

        HttpResponse res = new HttpResponseImpl();
        exception = new TwitterException("Test exception message", res);
        assertNotNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test exception message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());

        HttpResponse res = new HttpResponseImpl();
        exception = new TwitterException("Test exception message", res);
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test exception message");
        assertThrows(IllegalStateException.class, exception::getRetryAfter);

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");

        exception = new TwitterException("Test exception message", res);
        assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test exception message");
        assertFalse(exception.isCausedByNetworkIssue());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(503);

        exception = new TwitterException("Test exception message", res);
        assertFalse(exception.isCausedByNetworkIssue());

        Exception cause = new IOException("Test IO exception");
        exception = new TwitterException("Test exception message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test exception message");
        assertFalse(exception.exceededRateLimitation());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(400);

        exception = new TwitterException("Test exception message", res);
        assertTrue(exception.exceededRateLimitation());

        res = new HttpResponseImpl();
        res.setStatusCode(200);

        exception = new TwitterException("Test exception message", res);
        assertFalse(exception.exceededRateLimitation());

        exception = new TwitterException("Test exception message", null, 420);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test exception message");
        assertFalse(exception.resourceNotFound());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(404);

        exception = new TwitterException("Test exception message", res);
        assertTrue(exception.resourceNotFound());

        res = new HttpResponseImpl();
        res.setStatusCode(200);

        exception = new TwitterException("Test exception message", res);
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test exception message");
        assertEquals("3b1b08cb", exception.getExceptionCode());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(500);

        exception = new TwitterException("Test exception message", res);
        assertEquals("e1f8120b", exception.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception message 1");
        TwitterException exception2 = new TwitterException("Test exception message 1");
        assertTrue(exception1.equals(exception2));

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(500);

        exception1 = new TwitterException("Test exception message 2", res);
        exception2 = new TwitterException("Test exception message 2", res);
        assertTrue(exception1.equals(exception2));

        exception1 = new TwitterException("Test exception message 3");
        exception2 = new TwitterException("Test exception message 4");
        assertFalse(exception1.equals(exception2));

        exception1 = new TwitterException("Test exception message");
        exception2 = new TwitterException("Test exception message", null, 400);
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception message 1");
        TwitterException exception2 = new TwitterException("Test exception message 1");
        assertEquals(exception1.hashCode(), exception2.hashCode());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(500);

        exception1 = new TwitterException("Test exception message 2", res);
        exception2 = new TwitterException("Test exception message 2", res);
        assertEquals(exception1.hashCode(), exception2.hashCode());

        exception1 = new TwitterException("Test exception message 3");
        exception2 = new TwitterException("Test exception message 4");
        assertNotEquals(exception1.hashCode(), exception2.hashCode());

        exception1 = new TwitterException("Test exception message");
        exception2 = new TwitterException("Test exception message", null, 400);
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test exception message");
        String expected = "TwitterException{exceptionCode=[3b1b08cb], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=Twitter4J Version:[4.0.7]}";
        assertEquals(expected, exception.toString());

        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(500);

        exception = new TwitterException("Test exception message", res);
        expected = "TwitterException{exceptionCode=[e1f8120b], statusCode=500, retryAfter=0, rateLimitStatus=null, version=Twitter4J Version:[4.0.7]}";
        assertEquals(expected, exception.toString());
    }
}