package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TwitterExceptionTest {
    @Test
    public void testConstructor1() {
        TwitterException twitterException = new TwitterException("test message");
        assertEquals("test message", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor2() {
        Exception cause = new Exception("test cause");
        TwitterException twitterException = new TwitterException(cause);
        assertEquals("test cause", twitterException.getCause().getMessage());
        assertTrue(twitterException.getCause() instanceof TwitterException);
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testConstructor3() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        TwitterException twitterException = new TwitterException("test message", res);
        assertEquals("test message", twitterException.getMessage());
        assertEquals(420, twitterException.getStatusCode());
        assertEquals(60, twitterException.getRetryAfter());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.exceededRateLimitation());
    }

    @Test
    public void testConstructor4() {
        Exception cause = new Exception("test cause");
        TwitterException twitterException = new TwitterException("test message", cause);
        assertEquals("test message", twitterException.getMessage());
        assertEquals("test cause", twitterException.getCause().getMessage());
        assertFalse(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testConstructor5() {
        Exception cause = new Exception("test cause");
        TwitterException twitterException = new TwitterException("test message", cause, 500);
        assertEquals("test message", twitterException.getMessage());
        assertEquals("test cause", twitterException.getCause().getMessage());
        assertEquals(500, twitterException.getStatusCode());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("test message");
        assertEquals("", twitterException.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException twitterException = new TwitterException("test message");
        assertFalse(twitterException.nested);
        twitterException.setNested();
        assertTrue(twitterException.nested);
    }

    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("test message");
        TwitterException twitterException2 = new TwitterException("test message");
        assertTrue(twitterException1.equals(twitterException2));

        twitterException1.setNested();
        assertFalse(twitterException1.equals(twitterException2));

        twitterException2.setNested();
        assertTrue(twitterException1.equals(twitterException2));

        twitterException1 = new TwitterException("test message", 400);
        twitterException2 = new TwitterException("test message", 400);
        assertFalse(twitterException1.equals(twitterException2));

        twitterException2 = new TwitterException("test message", 400);
        assertTrue(twitterException1.equals(twitterException2));

        twitterException1.setRetryAfter(60);
        assertFalse(twitterException1.equals(twitterException2));

        twitterException2.setRetryAfter(60);
        assertTrue(twitterException1.equals(twitterException2));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException1 = new TwitterException("test message");
        TwitterException twitterException2 = new TwitterException("test message");
        assertEquals(twitterException1.hashCode(), twitterException2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("test message");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", twitterException.toString());
    }
}