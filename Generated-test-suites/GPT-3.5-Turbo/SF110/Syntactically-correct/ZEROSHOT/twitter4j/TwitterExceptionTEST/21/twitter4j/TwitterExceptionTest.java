import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        TwitterException twitterException = new TwitterException("Test exception");
        assertEquals("Test exception", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception("Test cause");
        TwitterException twitterException = new TwitterException(cause);
        assertEquals(cause, twitterException.getCause());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");

        TwitterException twitterException = new TwitterException("Test exception", res);
        assertEquals("Test exception\n420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", twitterException.getMessage());
        assertEquals(420, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertTrue(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals(60, twitterException.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionStringException() {
        Exception cause = new Exception("Test cause");
        TwitterException twitterException = new TwitterException("Test exception", cause);
        assertEquals("Test exception", twitterException.getMessage());
        assertEquals(cause, twitterException.getCause());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception cause = new Exception("Test cause");
        TwitterException twitterException = new TwitterException("Test exception", cause, 500);
        assertEquals("Test exception", twitterException.getMessage());
        assertEquals(cause, twitterException.getCause());
        assertEquals(500, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("Test exception");
        assertEquals(-1, twitterException.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test exception");
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test exception");
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("Test exception");
        try {
            twitterException.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException twitterException = new TwitterException("Test exception");
        assertFalse(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException = new TwitterException("Test exception");
        assertFalse(twitterException.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test exception");
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test exception");
        assertNotNull(twitterException.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException twitterException = new TwitterException("Test exception");
        twitterException.setNested();
        assertTrue(twitterException.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test exception 1");
        TwitterException twitterException2 = new TwitterException("Test exception 2");

        assertFalse(twitterException1.equals(null));
        assertFalse(twitterException1.equals(new Object()));
        assertTrue(twitterException1.equals(twitterException1));
        assertFalse(twitterException1.equals(twitterException2));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test exception");
        twitterException.setStatusCode(500);
        twitterException.setRetryAfter(60);

        assertEquals(16513, twitterException.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("Test exception");
        twitterException.setStatusCode(500);
        twitterException.setRetryAfter(60);
        twitterException.setRateLimitStatus(new RateLimitStatusJSONImpl());
        twitterException.setFeatureSpecificRateLimitStatus(new RateLimitStatusJSONImpl());

        assertEquals("TwitterException{statusCode=500, retryAfter=60, rateLimitStatus=RateLimitStatusJSONImpl{}}", twitterException.toString());
    }

    @Test
    public void testGetCause() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(500);

        String cause = TwitterException.getCause(res);
        assertEquals("500:Something is broken.  Please post to the group so the Twitter team can investigate.", cause);
    }
}