package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithString() {
        String msg = "Test message";
        TwitterException ex = new TwitterException(msg);
        assertEquals(msg, ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithException() {
        Exception cause = new Exception("Test exception");
        TwitterException ex = new TwitterException(cause);
        assertEquals(cause, ex.getCause());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithStringAndResponse() {
        String msg = "Test message";
        HttpResponse res = new HttpResponseImpl();
        TwitterException ex = new TwitterException(msg, res);
        assertEquals(getCause(res) + "\n" + msg, ex.getMessage());
        assertEquals(res.getStatusCode(), ex.getStatusCode());
        assertNotNull(ex.getRateLimitStatus());
        assertNotNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithStringAndException() {
        String msg = "Test message";
        Exception cause = new Exception("Test exception");
        TwitterException ex = new TwitterException(msg, cause);
        assertEquals(msg, ex.getMessage());
        assertEquals(cause, ex.getCause());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithStringExceptionAndStatusCode() {
        String msg = "Test message";
        Exception cause = new Exception("Test exception");
        int statusCode = 200;
        TwitterException ex = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, ex.getMessage());
        assertEquals(cause, ex.getCause());
        assertEquals(statusCode, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatus() {
            // Implement RateLimitStatus here
        };
        TwitterException ex = new TwitterException("");
        ex.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, ex.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus() {
            // Implement RateLimitStatus here
        };
        TwitterException ex = new TwitterException("");
        ex.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertEquals(featureSpecificRateLimitStatus, ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException ex = new TwitterException("");
        ex.statusCode = 420;
        ex.retryAfter = 10;
        assertEquals(10, ex.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterException() {
        TwitterException ex = new TwitterException("");
        ex.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Test IO exception");
        TwitterException ex = new TwitterException("");
        ex.initCause(ioException);
        assertTrue(ex.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException ex = new TwitterException("");
        ex.statusCode = 400;
        assertNotNull(ex.rateLimitStatus);
        assertTrue(ex.exceededRateLimitation());
        ex.statusCode = 420;
        assertFalse(ex.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException ex = new TwitterException("");
        ex.statusCode = 404;
        assertTrue(ex.resourceNotFound());
        ex.statusCode = 200;
        assertFalse(ex.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException ex = new TwitterException("");
        String exceptionCode = ex.getExceptionCode();
        assertNotNull(exceptionCode);
        assertFalse(exceptionCode.isEmpty());
    }

    @Test
    public void testEquals() {
        TwitterException ex1 = new TwitterException("");
        TwitterException ex2 = new TwitterException("");
        assertTrue(ex1.equals(ex2));
        assertTrue(ex2.equals(ex1));
        ex1.statusCode = 200;
        assertFalse(ex1.equals(ex2));
        assertFalse(ex2.equals(ex1));
        ex2.statusCode = 200;
        assertTrue(ex1.equals(ex2));
        assertTrue(ex2.equals(ex1));
        ex1.rateLimitStatus = new RateLimitStatus() {
            // Implement RateLimitStatus here
        };
        assertFalse(ex1.equals(ex2));
        assertFalse(ex2.equals(ex1));
        ex2.rateLimitStatus = new RateLimitStatus() {
            // Implement RateLimitStatus here
        };
        assertTrue(ex1.equals(ex2));
        assertTrue(ex2.equals(ex1));
    }

    @Test
    public void testHashCode() {
        TwitterException ex1 = new TwitterException("");
        TwitterException ex2 = new TwitterException("");
        assertTrue(ex1.hashCode() == ex2.hashCode());
        ex1.statusCode = 200;
        assertFalse(ex1.hashCode() == ex2.hashCode());
        ex2.statusCode = 200;
        assertTrue(ex1.hashCode() == ex2.hashCode());
        ex1.rateLimitStatus = new RateLimitStatus() {
            // Implement RateLimitStatus here
        };
        assertFalse(ex1.hashCode() == ex2.hashCode());
        ex2.rateLimitStatus = new RateLimitStatus() {
            // Implement RateLimitStatus here
        };
        assertTrue(ex1.hashCode() == ex2.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "TwitterException{exceptionCode=[00000000], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        TwitterException ex = new TwitterException("");
        assertEquals(expected, ex.toString());
    }

    private static String getCause(final HttpResponse res) {
        // Implement this method or mock it if necessary
    }
}