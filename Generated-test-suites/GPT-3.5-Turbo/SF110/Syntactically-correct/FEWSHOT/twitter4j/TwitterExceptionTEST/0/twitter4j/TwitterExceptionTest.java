package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TwitterExceptionTest {
    @Test
    public void testTwitterExceptionWithStringConstructor() {
        String msg = "Test Exception";
        TwitterException exception = new TwitterException(msg);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.equals(null));
        assertFalse(exception.equals(new Object()));
        assertFalse(exception.equals(new TwitterException("Different Exception")));
        assertEquals(exception.hashCode(), exception.hashCode());
        assertTrue(exception.toString().contains(msg));
    }

    @Test
    public void testTwitterExceptionWithExceptionConstructor() {
        String msg = "Test Exception";
        Exception cause = new Exception();
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.equals(null));
        assertFalse(exception.equals(new Object()));
        assertFalse(exception.equals(new TwitterException("Different Exception")));
        assertEquals(exception.hashCode(), exception.hashCode());
        assertTrue(exception.toString().contains(msg));
    }

    @Test
    public void testTwitterExceptionWithStringAndHttpResponseConstructor() {
        String msg = "Test Exception";
        HttpResponse res = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 500;
            }

            @Override
            public String getResponseHeader(String name) {
                return null;
            }
        };
        TwitterException exception = new TwitterException(msg, res);
        assertEquals(msg, exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.equals(null));
        assertFalse(exception.equals(new Object()));
        assertFalse(exception.equals(new TwitterException("Different Exception")));
        assertEquals(exception.hashCode(), exception.hashCode());
        assertTrue(exception.toString().contains(msg));
    }

    @Test
    public void testTwitterExceptionWithStringAndExceptionConstructor() {
        String msg = "Test Exception";
        Exception cause = new Exception();
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.equals(null));
        assertFalse(exception.equals(new Object()));
        assertFalse(exception.equals(new TwitterException("Different Exception")));
        assertEquals(exception.hashCode(), exception.hashCode());
        assertTrue(exception.toString().contains(msg));
    }

    @Test
    public void testTwitterExceptionWithStringExceptionAndStatusCodeConstructor() {
        String msg = "Test Exception";
        Exception cause = new Exception();
        int statusCode = 500;
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.equals(null));
        assertFalse(exception.equals(new Object()));
        assertFalse(exception.equals(new TwitterException("Different Exception")));
        assertEquals(exception.hashCode(), exception.hashCode());
        assertTrue(exception.toString().contains(msg));
    }

    @Test
    public void testGetStatusCode() {
        int statusCode = 500;
        TwitterException exception = new TwitterException("Test Exception");
        exception.statusCode = statusCode;
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test Exception");
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test Exception");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        int statusCode = 420;
        int retryAfter = 60;
        HttpResponse res = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return statusCode;
            }

            @Override
            public String getResponseHeader(String name) {
                return "60";
            }
        };
        TwitterException exception = new TwitterException("Test Exception", res);
        assertEquals(retryAfter, exception.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterIllegalStateException() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test Exception", cause);
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(new TwitterException("Test Exception").isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test Exception");
        assertFalse(exception1.exceededRateLimitation());

        TwitterException exception2 = new TwitterException("Test Exception", null, 400);
        assertFalse(exception2.exceededRateLimitation());

        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception3 = new TwitterException("Test Exception");
        exception3.rateLimitStatus = rateLimitStatus;
        assertTrue(exception3.exceededRateLimitation());

        TwitterException exception4 = new TwitterException("Test Exception", null, 420);
        assertTrue(exception4.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        int statusCode = 404;
        TwitterException exception = new TwitterException("Test Exception");
        exception.statusCode = statusCode;
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNotNull(exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.nested);
        exception.setNested();
        assertTrue(exception.nested);
    }
}