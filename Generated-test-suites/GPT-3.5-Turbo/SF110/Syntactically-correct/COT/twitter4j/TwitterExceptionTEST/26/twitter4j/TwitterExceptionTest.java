import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        String msg = "Test Message";
        TwitterException exception = new TwitterException(msg);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        String msg = "Test Message";
        HttpResponse res = new HttpResponse();
        TwitterException exception = new TwitterException(msg, res);
        assertEquals(getCause(res) + "\n" + msg, exception.getMessage());
        assertEquals(res.getStatusCode(), exception.getStatusCode());

        RateLimitStatus rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());

        RateLimitStatus featureSpecificRateLimitStatus = RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(res);
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());

        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringException() {
        String msg = "Test Message";
        Exception cause = new Exception();
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        String msg = "Test Message";
        Exception cause = new Exception();
        int statusCode = 500;
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetStatusCode() {
        int statusCode = 500;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("");
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("");
        exception.getStatusCode();
        // IllegalStateException is expected to be thrown, since the status code is not 420
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException();
        TwitterException exception = new TwitterException("");
        exception.initCause(cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        exception.rateLimitStatus = rateLimitStatus;
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        int statusCode = 404;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertTrue(exception.resourceNotFound());
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("");
        exception1.statusCode = 500;
        exception1.retryAfter = 60;
        exception1.rateLimitStatus = new RateLimitStatus();

        TwitterException exception2 = new TwitterException("");
        exception2.statusCode = 500;
        exception2.retryAfter = 60;
        exception2.rateLimitStatus = new RateLimitStatus();

        assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        int statusCode = 500;
        int retryAfter = 60;
        RateLimitStatus rateLimitStatus = new RateLimitStatus();

        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;
        exception.rateLimitStatus = rateLimitStatus;

        int expectedResult = 31 * (31 * (31 * statusCode + retryAfter) + rateLimitStatus.hashCode());
        assertEquals(expectedResult, exception.hashCode());
    }

    @Test
    public void testToString() {
        int statusCode = 500;
        int retryAfter = 60;
        RateLimitStatus rateLimitStatus = new RateLimitStatus();

        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;
        exception.rateLimitStatus = rateLimitStatus;

        String expectedString = "TwitterException{exceptionCode=[], statusCode=500, retryAfter=60, rateLimitStatus=" + rateLimitStatus + ", version=" + Version.getVersion() + '}';
        assertEquals(expectedString, exception.toString());
    }

    private static String getCause(HttpResponse res) {
        int statusCode = res.getStatusCode();
        String cause = null;
        switch (statusCode) {
            case 304: {
                cause = "There was no new data to return.";
                break;
            }
            case 400: {
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            }
            case 401: {
                cause = "Authentication credentials were missing or incorrect.";
                break;
            }
            case 403: {
                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                break;
            }
            case 404: {
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            }
            case 406: {
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            }
            case 420: {
                cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
                break;
            }
            case 500: {
                cause = "Something is broken.  Please post to the group so the Twitter team can investigate.";
                break;
            }
            case 502: {
                cause = "Twitter is down or being upgraded.";
                break;
            }
            case 503: {
                cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            }
            default: {
                cause = "";
                break;
            }
        }
        return statusCode + ":" + cause;
    }
}