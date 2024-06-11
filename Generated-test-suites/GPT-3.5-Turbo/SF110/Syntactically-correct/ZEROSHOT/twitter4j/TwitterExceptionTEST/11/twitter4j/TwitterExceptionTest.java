import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatus;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "Test message";
        TwitterException exception = new TwitterException(message);

        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
    }

    @Test
    public void testConstructorWithCause() {
        Exception cause = new NullPointerException();
        TwitterException exception = new TwitterException(cause);

        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
    }

    @Test
    public void testConstructorWithMessageAndResponse() {
        String message = "Test message";
        HttpResponse res = new HttpResponseImpl(); // create a mock HttpResponse
        TwitterException exception = new TwitterException(message, res);

        Assert.assertEquals(message + "\n" + TwitterException.getCause(res), exception.getMessage());
        Assert.assertEquals(res.getStatusCode(), exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNotNull(exception.getExceptionCode());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String message = "Test message";
        Exception cause = new NullPointerException();
        TwitterException exception = new TwitterException(message, cause);

        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
    }

    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        String message = "Test message";
        Exception cause = new NullPointerException();
        int statusCode = 500;
        TwitterException exception = new TwitterException(message, cause, statusCode);

        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(statusCode, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test exception");
        exception.statusCode = 420;
        exception.retryAfter = 60;

        int expectedRetryAfter = 60;
        int actualRetryAfter = exception.getRetryAfter();

        Assert.assertEquals(expectedRetryAfter, actualRetryAfter);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeNot420() {
        TwitterException exception = new TwitterException("Test exception");
        exception.statusCode = 200;

        exception.getRetryAfter();
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception");
        TwitterException exception2 = new TwitterException("Test exception");
        TwitterException exception3 = new TwitterException("Different exception");

        Assert.assertTrue(exception1.equals(exception2));
        Assert.assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception");
        TwitterException exception2 = new TwitterException("Test exception");

        Assert.assertEquals(exception1.hashCode(), exception2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test exception");
        exception.statusCode = 500;
        exception.retryAfter = 60;
        RateLimitStatus rateLimitStatus = new RateLimitStatusImpl(); // create a mock RateLimitStatus
        exception.rateLimitStatus = rateLimitStatus;
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusImpl(); // create a mock RateLimitStatus
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;

        String expectedString = "TwitterException{exceptionCode=[], statusCode=500, retryAfter=60, rateLimitStatus=" + rateLimitStatus +
                ", version=" + Version.getVersion() + "}";
        String actualString = exception.toString();

        Assert.assertEquals(expectedString, actualString);
    }
}

class HttpResponseImpl implements HttpResponse {
    @Override
    public int getStatusCode() {
        return 200;
    }

    @Override
    public String getResponseHeader(String name) {
        return null;
    }
}

class RateLimitStatusImpl implements RateLimitStatus {
    @Override
    public int getLimit() {
        return 100;
    }

    @Override
    public int getRemaining() {
        return 50;
    }

    @Override
    public int getResetTimeInSeconds() {
        return 1600000000;
    }

    @Override
    public int getSecondsUntilReset() {
        return 3600;
    }
}