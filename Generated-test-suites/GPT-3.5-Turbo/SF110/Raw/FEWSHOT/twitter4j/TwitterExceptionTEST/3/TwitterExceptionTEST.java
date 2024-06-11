
package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        TwitterException twitterException = new TwitterException("message");
        assertEquals("message", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("", twitterException.getExceptionCode());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception("cause");
        TwitterException twitterException = new TwitterException(cause);
        assertEquals(cause.toString(), twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("", twitterException.getExceptionCode());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        TwitterException twitterException = new TwitterException("message", res);
        assertEquals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.\nmessage", twitterException.getMessage());
        assertEquals(420, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertTrue(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("500594c8", twitterException.getExceptionCode());
    }

    @Test
    public void testTwitterExceptionStringException() {
        Exception cause = new Exception("cause");
        TwitterException twitterException = new TwitterException("message", cause);
        assertEquals("message", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("", twitterException.getExceptionCode());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception cause = new Exception("cause");
        TwitterException twitterException = new TwitterException("message", cause, 404);
        assertEquals("message", twitterException.getMessage());
        assertEquals(404, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertTrue(twitterException.resourceNotFound());
        assertEquals("1aaaa22", twitterException.getExceptionCode());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("message");
        try {
            twitterException.getRetryAfter();
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }

        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        twitterException = new TwitterException("message", res);
        assertEquals(60, twitterException.getRetryAfter());
    }

    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("message");
        TwitterException twitterException2 = new TwitterException("message");
        assertTrue(twitterException1.equals(twitterException1));
        assertTrue(twitterException1.equals(twitterException2));
        assertFalse(twitterException1.equals(new Object()));

        HttpResponse res = new HttpResponse();
        rateLimitStatus = new RateLimitStatusJSONImpl();
        rateLimitStatus.setRemaining(10);
        res.setResponseHeader("X-Rate-Limit-Remaining", "10");
        rateLimitStatus.setResetTimeInSeconds(1533150412);
        res.setResponseHeader("X-Rate-Limit-Reset", "1533150412");
        TwitterException twitterException3 = new TwitterException("message", res);
        assertFalse(twitterException1.equals(twitterException3));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException1 = new TwitterException("message");
        TwitterException twitterException2 = new TwitterException("message");
        assertEquals(twitterException1.hashCode(), twitterException2.hashCode());

        HttpResponse res = new HttpResponse();
        rateLimitStatus = new RateLimitStatusJSONImpl();
        rateLimitStatus.setRemaining(10);
        res.setResponseHeader("X-Rate-Limit-Remaining", "10");
        rateLimitStatus.setResetTimeInSeconds(1533150412);
        res.setResponseHeader("X-Rate-Limit-Reset", "1533150412");
        TwitterException twitterException3 = new TwitterException("message", res);
        assertNotEquals(twitterException1.hashCode(), twitterException3.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("message");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=###version###}", twitterException.toString());

        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        rateLimitStatus = new RateLimitStatusJSONImpl();
        rateLimitStatus.setRemaining(10);
        res.setResponseHeader("X-Rate-Limit-Remaining", "10");
        rateLimitStatus.setResetTimeInSeconds(1533150412);
        res.setResponseHeader("X-Rate-Limit-Reset", "1533150412");
        TwitterException twitterException = new TwitterException("message", res);
        assertEquals("TwitterException{exceptionCode=[], statusCode=420, retryAfter=60, rateLimitStatus=RateLimitStatusJSONImpl{remaining=10, limit=0, resetTimeInSeconds=1533150412}, version=###version###}", twitterException.toString());
    }
}
