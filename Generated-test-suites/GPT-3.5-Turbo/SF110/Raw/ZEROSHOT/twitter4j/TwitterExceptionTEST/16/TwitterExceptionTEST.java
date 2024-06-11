package twitter4j;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.internal.http.HttpResponse;

import java.io.IOException;

public class TwitterExceptionTest {

    private final String msg = "Test message";
    private final Exception cause = new Exception();
    private final int statusCode = 404;
    private final HttpResponse res = new HttpResponse() {
        @Override
        public int getStatusCode() {
            return statusCode;
        }

        @Override
        public String getResponseHeader(String name) {
            return null;
        }

        @Override
        public ResponseBody asResponseBody() throws IOException {
            return null;
        }
    };

    @Test
    public void testConstructorWithMsg() {
        TwitterException exception = new TwitterException(msg);

        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithCause() {
        TwitterException exception = new TwitterException(cause);

        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testConstructorWithMsgAndRes() {
        TwitterException exception = new TwitterException(msg, res);

        Assert.assertEquals(statusCode + ":" + getCause(res) + "\n" + msg, exception.getMessage());
        Assert.assertEquals(statusCode, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMsgAndCause() {
        TwitterException exception = new TwitterException(msg, cause);

        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        TwitterException exception = new TwitterException(msg, cause, statusCode);

        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(statusCode, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException(msg, res);

        int retryAfter = exception.getRetryAfter();

        Assert.assertEquals(-1, retryAfter);
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException(msg, cause);

        boolean isNetworkIssue = exception.isCausedByNetworkIssue();

        Assert.assertTrue(isNetworkIssue);
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException(msg, res);

        boolean exceededRateLimitation = exception.exceededRateLimitation();

        Assert.assertTrue(exceededRateLimitation);
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException(msg, res);

        boolean resourceNotFound = exception.resourceNotFound();

        Assert.assertFalse(resourceNotFound);
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException(msg, res);

        String exceptionCode = exception.getExceptionCode();

        Assert.assertNotNull(exceptionCode);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException(msg, res);
        TwitterException exception2 = new TwitterException(msg, res);

        boolean isEqual = exception1.equals(exception2);

        Assert.assertTrue(isEqual);
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException(msg, res);

        int hashCode = exception.hashCode();

        Assert.assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException(msg, res);

        String toString = exception.toString();

        Assert.assertNotNull(toString);
    }

    private String getCause(final HttpResponse res) {
        final int statusCode = res.getStatusCode();
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