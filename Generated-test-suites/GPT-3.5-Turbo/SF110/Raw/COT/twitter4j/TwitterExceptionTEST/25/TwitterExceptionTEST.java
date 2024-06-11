import org.junit.*;
import twitter4j.*;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseCode;

public class TwitterExceptionTest {

    @Test
    public void testConstructor1() {
        TwitterException exception = new TwitterException("Test message");
        String expectedMessage = "Test message";
        Assert.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testConstructor2() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructor3() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        String msg = "Test message";
        TwitterException exception = new TwitterException(msg, res);
        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(420, exception.getStatusCode());
    }

    @Test
    public void testConstructor4() {
        String msg = "Test message";
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(msg, cause);
        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructor5() {
        String msg = "Test message";
        Exception cause = new Exception("Test cause");
        int statusCode = 500;
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    public void testGetStatusCode() {
        int statusCode = 500;
        TwitterException exception = new TwitterException("Test message", null, statusCode);
        Assert.assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        Assert.assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test message", res);
        Assert.assertEquals(60, exception.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter_NonExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue_True() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("", cause);
        Assert.assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testIsCausedByNetworkIssue_False() {
        TwitterException exception = new TwitterException("Test message");
        Assert.assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation_True() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        TwitterException exception = new TwitterException("Test message", res);
        Assert.assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitation_False() {
        TwitterException exception = new TwitterException("Test message");
        Assert.assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound_True() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(404);
        TwitterException exception = new TwitterException("Test message", res);
        Assert.assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testResourceNotFound_False() {
        TwitterException exception = new TwitterException("Test message");
        Assert.assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        Assert.assertNotNull(exception.getExceptionCode());
    }

    @Test
    public void testEquals_True() {
        TwitterException exception1 = new TwitterException("Test message");
        TwitterException exception2 = new TwitterException("Test message");
        Assert.assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testEquals_False() {
        TwitterException exception1 = new TwitterException("Test message 1");
        TwitterException exception2 = new TwitterException("Test message 2");
        Assert.assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test message");
        Assert.assertNotNull(exception.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        String expectedString = "TwitterException{statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        Assert.assertEquals(expectedString, exception.toString());
    }

}