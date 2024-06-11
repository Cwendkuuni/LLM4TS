package twitter4j;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructor() {
        TwitterException exception = new TwitterException("Test exception");
        assertNotNull(exception);
        assertEquals("Test exception", exception.getMessage());
    }

    @Test
    public void testConstructorWithCause() {
        IOException cause = new IOException("Test cause");
        TwitterException exception = new TwitterException(cause);
        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithMsgAndResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);

        TwitterException exception = new TwitterException("Test exception", response);
        assertNotNull(exception);
        assertEquals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(0, exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMsgAndCause() {
        IOException cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test exception", cause);
        assertNotNull(exception);
        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        IOException cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test exception", cause, 500);
        assertNotNull(exception);
        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test exception");
        exception.statusCode = 403;

        assertEquals(403, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test exception");
        exception.rateLimitStatus = new RateLimitStatusImpl();

        assertNotNull(exception.getRateLimitStatus());
        assertEquals(RateLimitStatusImpl.class, exception.getRateLimitStatus().getClass());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test exception");
        exception.featureSpecificRateLimitStatus = new RateLimitStatusImpl();

        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(RateLimitStatusImpl.class, exception.getFeatureSpecificRateLimitStatus().getClass());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test exception");

        exception.statusCode = 420;
        exception.retryAfter = 60;

        assertEquals(60, exception.getRetryAfter());

        exception.statusCode = 200;

        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Pass
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test exception", cause);

        assertTrue(exception.isCausedByNetworkIssue());

        exception = new TwitterException("Test exception");

        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test exception");

        assertFalse(exception.exceededRateLimitation());

        exception.statusCode = 400;
        exception.rateLimitStatus = new RateLimitStatusImpl();

        assertTrue(exception.exceededRateLimitation());

        exception.statusCode = 420;
        exception.rateLimitStatus = null;

        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test exception");

        assertFalse(exception.resourceNotFound());

        exception.statusCode = 404;

        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test exception");

        assertEquals("c1d4d", exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test exception");

        assertFalse(exception.nested);

        exception.setNested();

        assertTrue(exception.nested);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception");
        TwitterException exception2 = new TwitterException("Test exception");

        assertTrue(exception1.equals(exception2));

        exception1.statusCode = 400;
        exception1.rateLimitStatus = new RateLimitStatusImpl();
        exception2.statusCode = 400;
        exception2.rateLimitStatus = new RateLimitStatusImpl();

        assertTrue(exception1.equals(exception2));

        exception1.rateLimitStatus = null;
        exception2.rateLimitStatus = new RateLimitStatusImpl();

        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test exception");

        assertEquals(0, exception.hashCode());

        exception.statusCode = 400;
        exception.rateLimitStatus = new RateLimitStatusImpl();
        exception.retryAfter = 60;

        assertEquals(1003173, exception.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test exception");

        assertEquals("TwitterException{exceptionCode=[c1d4d], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=UNKNOWN}", exception.toString());
    }

    @Test
    public void testGetCause() {
        HttpResponse response = new HttpResponse();

        response.setStatusCode(404);

        assertEquals("404:The URI requested is invalid or the resource requested, such as a user, does not exists.", TwitterException.getCause(response));
    }
}