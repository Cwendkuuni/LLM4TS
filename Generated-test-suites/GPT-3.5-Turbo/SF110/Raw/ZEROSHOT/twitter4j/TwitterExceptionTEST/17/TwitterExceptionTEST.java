package twitter4j;

import org.junit.Assert;
import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterException() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertEquals("Test Message", twitterException.getMessage());
        Assert.assertEquals(-1, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(twitterException.isCausedByNetworkIssue());
        Assert.assertFalse(twitterException.exceededRateLimitation());
        Assert.assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Test Exception");
        TwitterException twitterException = new TwitterException(cause);
        Assert.assertSame(cause, twitterException.getCause());
        Assert.assertEquals(-1, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(twitterException.isCausedByNetworkIssue());
        Assert.assertFalse(twitterException.exceededRateLimitation());
        Assert.assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(500);
        httpResponse.addResponseHeader("Retry-After", "60");
        TwitterException twitterException = new TwitterException("Test Message", httpResponse);
        Assert.assertEquals("Test Message", twitterException.getMessage());
        Assert.assertEquals(500, twitterException.getStatusCode());
        Assert.assertNotNull(twitterException.getRateLimitStatus());
        Assert.assertNotNull(twitterException.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(60, twitterException.getRetryAfter());
        Assert.assertFalse(twitterException.isCausedByNetworkIssue());
        Assert.assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageAndCause() {
        Exception cause = new Exception("Test Exception");
        TwitterException twitterException = new TwitterException("Test Message", cause);
        Assert.assertSame(cause, twitterException.getCause());
        Assert.assertEquals(-1, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(twitterException.isCausedByNetworkIssue());
        Assert.assertFalse(twitterException.exceededRateLimitation());
        Assert.assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("Test Exception");
        TwitterException twitterException = new TwitterException("Test Message", cause, 500);
        Assert.assertSame(cause, twitterException.getCause());
        Assert.assertEquals(500, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(twitterException.isCausedByNetworkIssue());
        Assert.assertFalse(twitterException.exceededRateLimitation());
        Assert.assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertEquals(-1, twitterException.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("Test Message");
        twitterException.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertFalse(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertFalse(twitterException.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertNotNull(twitterException.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException twitterException = new TwitterException("Test Message");
        twitterException.setNested();
        Assert.assertTrue(twitterException.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test Message 1");
        TwitterException twitterException2 = new TwitterException("Test Message 2");
        Assert.assertFalse(twitterException1.equals(twitterException2));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertNotEquals(0, twitterException.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("Test Message");
        Assert.assertNotNull(twitterException.toString());
    }

    private static class HttpResponse implements HttpResponseCode {

        private int statusCode;
        private Map<String, String> responseHeaders;

        public HttpResponse() {
            this.statusCode = -1;
            this.responseHeaders = new HashMap<>();
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public void addResponseHeader(String key, String value) {
            responseHeaders.put(key, value);
        }

        public int getStatusCode() {
            return this.statusCode;
        }

        public String getResponseHeader(String key) {
            return responseHeaders.get(key);
        }
    }
}