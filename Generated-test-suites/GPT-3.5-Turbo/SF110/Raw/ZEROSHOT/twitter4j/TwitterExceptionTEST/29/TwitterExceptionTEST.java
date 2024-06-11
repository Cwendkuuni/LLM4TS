import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructor1() {
        String msg = "Test Exception";
        TwitterException exception = new TwitterException(msg);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(false, exception.nested);
    }
    
    @Test
    public void testConstructor2() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(true, exception.nested);
    }
    
    @Test
    public void testConstructor3() {
        String msg = "Test Exception";
        HttpResponse res = new HttpResponseImpl();
        TwitterException exception = new TwitterException(msg, res);
        assertEquals(getCause(res) + "\n" + msg, exception.getMessage());
        assertEquals(res.getStatusCode(), exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(false, exception.nested);
    }
    
    @Test
    public void testConstructor4() {
        String msg = "Test Exception";
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(false, exception.nested);
    }
    
    @Test
    public void testConstructor5() {
        String msg = "Test Exception";
        Exception cause = new Exception("Test Cause");
        int statusCode = 500;
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(false, exception.nested);
    }
    
    @Test
    public void testGetStatusCode() {
        int statusCode = 400;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertEquals(statusCode, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("");
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        int statusCode = 420;
        int retryAfter = 10;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;
        assertEquals(retryAfter, exception.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("");
        exception.initCause(new IOException());
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception1 = new TwitterException("");
        exception1.statusCode = statusCode;
        exception1.rateLimitStatus = rateLimitStatus;
        assertTrue(exception1.exceededRateLimitation());
        
        int statusCode2 = 420;
        TwitterException exception2 = new TwitterException("");
        exception2.statusCode = statusCode2;
        assertFalse(exception2.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        int statusCode = 404;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        String filter = "twitter4j";
        String exceptionCode = "123456789";
        TwitterException exception = new TwitterException("");
        exception.FILTER[0] = filter;
        assertEquals(exceptionCode, exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("");
        exception.setNested();
        assertTrue(exception.nested);
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("");
        exception1.statusCode = 200;
        exception1.rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception2 = new TwitterException("");
        exception2.statusCode = 200;
        exception2.rateLimitStatus = new RateLimitStatusJSONImpl();
        assertTrue(exception1.equals(exception2));
        
        exception2.statusCode = 500;
        assertFalse(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("");
        int statusCode = 200;
        int retryAfter = 10;
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;
        int expectedHashCode = 31 * (31 * statusCode + retryAfter);
        expectedHashCode = 31 * expectedHashCode + exception.rateLimitStatus.hashCode();
        assertEquals(expectedHashCode, exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception1 = new TwitterException("");
        assertTrue(exception1.toString().contains("exceptionCode=["));
        assertTrue(exception1.toString().contains("statusCode="));
        assertTrue(exception1.toString().contains("retryAfter="));
        assertTrue(exception1.toString().contains("rateLimitStatus="));
        assertTrue(exception1.toString().contains("version="));
        
        TwitterException exception2 = new TwitterException("");
        exception2.nested = true;
        assertFalse(exception2.toString().contains("exceptionCode=["));
        assertTrue(exception2.toString().contains("statusCode="));
        assertTrue(exception2.toString().contains("retryAfter="));
        assertTrue(exception2.toString().contains("rateLimitStatus="));
        assertTrue(exception2.toString().contains("version="));
    }
    
    private class HttpResponseImpl implements HttpResponse {
        @Override
        public int getStatusCode() {
            return 200;
        }
        
        @Override
        public String getResponseHeader(String name) {
            return null;
        }
    }
    
    private class RateLimitStatusJSONImpl implements RateLimitStatus {
        @Override
        public int getRemaining() {
            return 0;
        }
        
        @Override
        public int getLimit() {
            return 0;
        }
        
        @Override
        public int getResetTimeInSeconds() {
            return 0;
        }
        
        @Override
        public int getSecondsUntilReset() {
            return 0;
        }
    }
    
    private class Version {
        public static String getVersion() {
            return "1.0";
        }
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