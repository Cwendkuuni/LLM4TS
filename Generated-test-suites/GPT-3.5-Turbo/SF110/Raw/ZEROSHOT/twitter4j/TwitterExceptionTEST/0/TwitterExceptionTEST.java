import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatus;
import twitter4j.internal.http.HttpResponse;
import java.io.IOException;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionConstructorWithMsg() {
        String msg = "Test Exception";
        TwitterException twitterException = new TwitterException(msg);

        Assert.assertEquals(msg, twitterException.getMessage());
        Assert.assertEquals(-1, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getRateLimitStatus());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithCause() {
        IOException cause = new IOException("Test Cause");
        TwitterException twitterException = new TwitterException(cause);

        Assert.assertEquals(cause, twitterException.getCause());
        Assert.assertEquals(-1, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getRateLimitStatus());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithMsgAndHttpResponse() {
        String msg = "Test Exception";
        HttpResponse res = new HttpResponse();
        TwitterException twitterException = new TwitterException(msg, res);

        Assert.assertEquals(getCause(res) + "\n" + msg, twitterException.getMessage());
        Assert.assertEquals(res.getStatusCode(), twitterException.getStatusCode());
        Assert.assertNotNull(twitterException.getRateLimitStatus());
        Assert.assertNotNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithMsgAndCause() {
        String msg = "Test Exception";
        Exception cause = new Exception("Test Cause");
        TwitterException twitterException = new TwitterException(msg, cause);

        Assert.assertEquals(msg, twitterException.getMessage());
        Assert.assertEquals(cause, twitterException.getCause());
        Assert.assertEquals(-1, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getRateLimitStatus());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithMsgCauseAndStatusCode() {
        String msg = "Test Exception";
        Exception cause = new Exception("Test Cause");
        int statusCode = 500;
        TwitterException twitterException = new TwitterException(msg, cause, statusCode);

        Assert.assertEquals(msg, twitterException.getMessage());
        Assert.assertEquals(cause, twitterException.getCause());
        Assert.assertEquals(statusCode, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getRateLimitStatus());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfterWithValidStatusCode() {
        TwitterException twitterException = new TwitterException("Test Exception");

        try {
            twitterException.getRetryAfter();
        } catch (IllegalStateException e) {
            Assert.assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test Cause");
        TwitterException twitterException = new TwitterException(cause);

        Assert.assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitationWithStatusCode400() {
        TwitterException twitterException = new TwitterException("Test Exception");
        twitterException.statusCode = 400;
        twitterException.rateLimitStatus = new RateLimitStatus();

        Assert.assertTrue(twitterException.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithStatusCode420() {
        TwitterException twitterException = new TwitterException("Test Exception");
        twitterException.statusCode = 420;

        Assert.assertTrue(twitterException.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithInvalidStatusCode() {
        TwitterException twitterException = new TwitterException("Test Exception");
        twitterException.statusCode = 401;

        Assert.assertFalse(twitterException.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test Exception");
        twitterException.statusCode = 404;

        Assert.assertTrue(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test Exception");

        Assert.assertNotNull(twitterException.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException twitterException = new TwitterException("Test Exception");
        twitterException.setNested();

        Assert.assertTrue(twitterException.nested);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        TwitterException twitterException1 = new TwitterException("Test Exception");
        TwitterException twitterException2 = new TwitterException("Test Exception");

        Assert.assertEquals(twitterException1, twitterException2);
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        TwitterException twitterException1 = new TwitterException("Test Exception 1");
        TwitterException twitterException2 = new TwitterException("Test Exception 2");

        Assert.assertNotEquals(twitterException1, twitterException2);
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test Exception");

        Assert.assertEquals(twitterException.statusCode + 31 * twitterException.retryAfter, twitterException.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("Test Exception");     

        Assert.assertNotNull(twitterException.toString());
    }

    private static String getCause(final HttpResponse res) {
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