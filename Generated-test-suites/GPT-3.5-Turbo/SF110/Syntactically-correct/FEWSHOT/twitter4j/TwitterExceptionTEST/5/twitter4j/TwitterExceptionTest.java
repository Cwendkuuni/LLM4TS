
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.addResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test Exception", res);
        assertEquals("Test Exception\n420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNotNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringException() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception", cause, 500);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testGetStatusCode() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(200);
        TwitterException exception = new TwitterException("Test Exception", res);
        assertEquals(200, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse res = new HttpResponse();
        res.addResponseHeader("X-RateLimit-Limit", "1000");
        res.addResponseHeader("X-RateLimit-Remaining", "500");
        res.addResponseHeader("X-RateLimit-Reset", "1500000000");
        TwitterException exception = new TwitterException("Test Exception", res);
        assertNotNull(exception.getRateLimitStatus());
        assertEquals(1000, exception.getRateLimitStatus().getLimit());
        assertEquals(500, exception.getRateLimitStatus().getRemaining());
        assertEquals(1500000000, exception.getRateLimitStatus().getResetTimeInSeconds());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse res = new HttpResponse();
        res.addResponseHeader("X-FeatureRateLimit-Limit", "100");
        res.addResponseHeader("X-FeatureRateLimit-Remaining", "50");
        res.addResponseHeader("X-FeatureRateLimit-Reset", "1500000000");
        TwitterException exception = new TwitterException("Test Exception", res);
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(100, exception.getFeatureSpecificRateLimitStatus().getLimit());
        assertEquals(50, exception.getFeatureSpecificRateLimitStatus().getRemaining());
        assertEquals(1500000000, exception.getFeatureSpecificRateLimitStatus().getResetTimeInSeconds());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        HttpResponse res = new HttpResponse();
        TwitterException exception = new TwitterException("Test Exception", res);
        exception.getRetryAfter();
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        TwitterException exception = new TwitterException("Test Exception", res);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(404);
        TwitterException exception = new TwitterException("Test Exception", res);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        String exceptionCode = exception.getExceptionCode();
        assertNotNull(exceptionCode);
        assertEquals("e10c10n01", exceptionCode);
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.isNested());
        exception.setNested();
        assertTrue(exception.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        TwitterException exception3 = new TwitterException(new Exception());
        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        String expectedString = "TwitterException{exceptionCode=[e10c10n01], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expectedString, exception.toString());
    }

    private class HttpResponse implements TwitterResponse {
        private int statusCode;
        private Map<String, List<String>> responseHeaders;

        public HttpResponse() {
            responseHeaders = new HashMap<>();
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public void addResponseHeader(String key, String value) {
            if (!responseHeaders.containsKey(key)) {
                responseHeaders.put(key, new ArrayList<>());
            }
            responseHeaders.get(key).add(value);
        }

        @Override
        public Map<String, List<String>> getResponseHeaders() {
            return responseHeaders;
        }
    }
}
