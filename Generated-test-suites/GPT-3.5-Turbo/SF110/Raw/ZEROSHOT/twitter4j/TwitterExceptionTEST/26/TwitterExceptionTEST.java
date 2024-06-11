import org.junit.Assert;
import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String msg = "Test exception";
        TwitterException ex = new TwitterException(msg);
        Assert.assertEquals(msg, ex.getMessage());
        Assert.assertEquals(-1, ex.getStatusCode());
        Assert.assertNull(ex.getRateLimitStatus());
        Assert.assertNull(ex.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(ex.exceededRateLimitation());
        Assert.assertFalse(ex.resourceNotFound());
    }

    @Test
    public void testConstructorWithCause() {
        String msg = "Test exception";
        Exception cause = new Exception("Test cause");
        TwitterException ex = new TwitterException(msg, cause);
        Assert.assertEquals(msg, ex.getMessage());
        Assert.assertEquals(-1, ex.getStatusCode());
        Assert.assertNull(ex.getRateLimitStatus());
        Assert.assertNull(ex.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(ex.exceededRateLimitation());
        Assert.assertFalse(ex.resourceNotFound());
    }

    @Test
    public void testConstructorWithMessageAndHttpResponse() {
        String msg = "Test exception";
        HttpResponse res = new HttpResponseImpl();
        TwitterException ex = new TwitterException(msg, res);
        Assert.assertEquals(getCause(res) + "\n" + msg, ex.getMessage());
        Assert.assertEquals(res.getStatusCode(), ex.getStatusCode());
        Assert.assertNotNull(ex.getRateLimitStatus());
        Assert.assertNotNull(ex.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(ex.exceededRateLimitation());
        Assert.assertFalse(ex.resourceNotFound());
    }

    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        String msg = "Test exception";
        Exception cause = new Exception("Test cause");
        int statusCode = 200;
        TwitterException ex = new TwitterException(msg, cause, statusCode);
        Assert.assertEquals(msg, ex.getMessage());
        Assert.assertEquals(statusCode, ex.getStatusCode());
        Assert.assertNull(ex.getRateLimitStatus());
        Assert.assertNull(ex.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(ex.exceededRateLimitation());
        Assert.assertFalse(ex.resourceNotFound());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException ex = new TwitterException("Test exception");
        ex.statusCode = 420;
        ex.retryAfter = 60;
        Assert.assertEquals(60, ex.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterNotExceededRateLimitation() {
        TwitterException ex = new TwitterException("Test exception");
        ex.statusCode = 200;
        ex.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException ex = new TwitterException("Test exception");
        Assert.assertFalse(ex.isCausedByNetworkIssue());

        ex = new TwitterException(new IOException("Network issue"));
        Assert.assertTrue(ex.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException ex = new TwitterException("Test exception");
        ex.statusCode = 400;
        Assert.assertFalse(ex.exceededRateLimitation());

        ex = new TwitterException("Test exception");
        ex.statusCode = 420;
        Assert.assertTrue(ex.exceededRateLimitation());

        ex = new TwitterException("Test exception");
        Assert.assertFalse(ex.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException ex = new TwitterException("Test exception");
        ex.statusCode = 404;
        Assert.assertTrue(ex.resourceNotFound());

        ex = new TwitterException("Test exception");
        Assert.assertFalse(ex.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException ex = new TwitterException("Test exception");
        Assert.assertNotNull(ex.getExceptionCode());
    }


}