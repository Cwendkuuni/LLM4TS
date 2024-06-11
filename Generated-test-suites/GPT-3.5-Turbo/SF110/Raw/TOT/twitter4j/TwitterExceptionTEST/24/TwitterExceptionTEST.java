import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    private TwitterException twitterException;

    @Before
    public void setUp() {
        twitterException = new TwitterException("Test Exception");
    }

    @After
    public void tearDown() {
        twitterException = null;
    }

    @Test
    public void testTwitterExceptionConstructorWithMessage() {
        assert twitterException.getMessage().equals("Test Exception");
        assert twitterException.getStatusCode() == -1;
        assert twitterException.getRateLimitStatus() == null;
        assert twitterException.getFeatureSpecificRateLimitStatus() == null;
    }

    @Test
    public void testTwitterExceptionConstructorWithException() {
        Exception cause = new NullPointerException();
        TwitterException twitterExceptionWithCause = new TwitterException(cause);
        assert twitterExceptionWithCause.getMessage().equals("java.lang.NullPointerException");
        assert twitterExceptionWithCause.getStatusCode() == -1;
        assert twitterExceptionWithCause.getRateLimitStatus() == null;
        assert twitterExceptionWithCause.getFeatureSpecificRateLimitStatus() == null;
        assert twitterExceptionWithCause.isCausedByNetworkIssue();
        assert !twitterExceptionWithCause.exceededRateLimitation();
        assert !twitterExceptionWithCause.resourceNotFound();
    }

    @Test
    public void testTwitterExceptionConstructorWithMessageAndResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        TwitterException twitterExceptionWithResponse = new TwitterException("Test Exception", response);
        assert twitterExceptionWithResponse.getMessage().equals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.\nTest Exception");
        assert twitterExceptionWithResponse.getStatusCode() == 420;
        assert twitterExceptionWithResponse.getRetryAfter() == 60;
        assert twitterExceptionWithResponse.getRateLimitStatus() != null;
        assert twitterExceptionWithResponse.getFeatureSpecificRateLimitStatus() != null;
        assert !twitterExceptionWithResponse.isCausedByNetworkIssue();
        assert twitterExceptionWithResponse.exceededRateLimitation();
        assert !twitterExceptionWithResponse.resourceNotFound();
    }

    @Test
    public void testTwitterExceptionConstructorWithMessageAndException() {
        Exception cause = new NullPointerException();
        TwitterException twitterExceptionWithCause = new TwitterException("Test Exception", cause);
        assert twitterExceptionWithCause.getMessage().equals("Test Exception");
        assert twitterExceptionWithCause.getCause() == cause;
        assert !twitterExceptionWithCause.isCausedByNetworkIssue();
        assert !twitterExceptionWithCause.exceededRateLimitation();
        assert !twitterExceptionWithCause.resourceNotFound();
    }

    @Test
    public void testTwitterExceptionConstructorWithMessageExceptionAndStatusCode() {
        Exception cause = new NullPointerException();
        int statusCode = 500;
        TwitterException twitterExceptionWithCauseAndStatusCode = new TwitterException("Test Exception", cause, statusCode);
        assert twitterExceptionWithCauseAndStatusCode.getMessage().equals("Test Exception");
        assert twitterExceptionWithCauseAndStatusCode.getCause() == cause;
        assert twitterExceptionWithCauseAndStatusCode.getStatusCode() == 500;
        assert !twitterExceptionWithCauseAndStatusCode.isCausedByNetworkIssue();
        assert !twitterExceptionWithCauseAndStatusCode.exceededRateLimitation();
        assert !twitterExceptionWithCauseAndStatusCode.resourceNotFound();
    }

    @Test
    public void testGetStatusCode() {
        assert twitterException.getStatusCode() == -1;
    }

    @Test
    public void testGetRateLimitStatus() {
        assert twitterException.getRateLimitStatus() == null;
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assert twitterException.getFeatureSpecificRateLimitStatus() == null;
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        twitterException.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssueWhenCausedByNetworkIssue() {
        Exception cause = new IOException();
        TwitterException twitterExceptionWithIOException = new TwitterException("Test Exception", cause);
        assert twitterExceptionWithIOException.isCausedByNetworkIssue();
    }

    @Test
    public void testIsCausedByNetworkIssueWhenNotCausedByNetworkIssue() {
        Exception cause = new NullPointerException();
        TwitterException twitterExceptionWithNullPointerException = new TwitterException("Test Exception", cause);
        assert !twitterExceptionWithNullPointerException.isCausedByNetworkIssue();
    }

    @Test
    public void testExceededRateLimitationWhenExceeded() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        TwitterException twitterExceptionWithResponse = new TwitterException("Test Exception", response);
        assert twitterExceptionWithResponse.exceededRateLimitation();
    }

    @Test
    public void testExceededRateLimitationWhenNotExceeded() {
        Exception cause = new NullPointerException();
        TwitterException twitterExceptionWithCause = new TwitterException("Test Exception", cause);
        assert !twitterExceptionWithCause.exceededRateLimitation();
    }

    @Test
    public void testResourceNotFoundWhenResourceNotFound() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(404);
        TwitterException twitterExceptionWithResponse = new TwitterException("Test Exception", response);
        assert twitterExceptionWithResponse.resourceNotFound();
    }

    @Test
    public void testResourceNotFoundWhenNotResourceNotFound() {
        Exception cause = new NullPointerException();
        TwitterException twitterExceptionWithCause = new TwitterException("Test Exception", cause);
        assert !twitterExceptionWithCause.resourceNotFound();
    }

    @Test
    public void testGetExceptionCode() {
        assert twitterException.getExceptionCode().equals("");
    }

    @Test
    public void testEqualsWhenEqual() {
        Exception cause = new NullPointerException();
        TwitterException twitterException1 = new TwitterException("Test Exception", cause, 500);
        TwitterException twitterException2 = new TwitterException("Test Exception", cause, 500);
        assert twitterException1.equals(twitterException2);
    }

    @Test
    public void testEqualsWhenNotEqual() {
        Exception cause = new NullPointerException();
        TwitterException twitterException1 = new TwitterException("Test Exception", cause, 500);
        TwitterException twitterException2 = new TwitterException("Different Exception", cause, 500);
        assert !twitterException1.equals(twitterException2);
    }

    @Test
    public void testHashCode() {
        assert twitterException.hashCode() == -1;
    }

    @Test
    public void testToString() {
        assert twitterException.toString().equals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=}");
    }
}