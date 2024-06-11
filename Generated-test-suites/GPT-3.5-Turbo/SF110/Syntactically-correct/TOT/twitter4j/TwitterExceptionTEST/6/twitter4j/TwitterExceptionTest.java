import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructor1() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testConstructor2() {
        Exception nestedException = new Exception("Nested Exception");
        TwitterException exception = new TwitterException(nestedException);
        assertEquals(nestedException.getMessage(), exception.getCause().getMessage());
        assertTrue(exception.getCause() instanceof TwitterException);
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testConstructor3() {
        HttpResponse response = new HttpResponseMock(400, "Retry-After: 5");
        TwitterException exception = new TwitterException("Test Exception", response);
        assertEquals("400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.\nTest Exception", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(5, exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testConstructor4() {
        Exception nestedException = new Exception("Nested Exception");
        TwitterException exception = new TwitterException("Test Exception", nestedException);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(nestedException, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testConstructor5() {
        Exception nestedException = new Exception("Nested Exception");
        TwitterException exception = new TwitterException("Test Exception", nestedException, 500);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(nestedException, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals(-1, exception.getStatusCode());
        
        HttpResponse response = new HttpResponseMock(404);
        exception = new TwitterException("Test Exception", response);
        assertEquals(404, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNull(exception.getRateLimitStatus());
        
        HttpResponse response = new HttpResponseMock(200);
        exception = new TwitterException("Test Exception", response);
        assertNull(exception.getRateLimitStatus());
        
        response = new HttpResponseMock(400, "x-rate-limit-limit: 1000", "x-rate-limit-remaining: 500", "x-rate-limit-reset: 1577865600");
        exception = new TwitterException("Test Exception", response);
        assertNotNull(exception.getRateLimitStatus());
        assertEquals(1000, exception.getRateLimitStatus().getLimit());
        assertEquals(500, exception.getRateLimitStatus().getRemaining());
        assertEquals(1577865600, exception.getRateLimitStatus().getResetTimeInSeconds());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        
        HttpResponse response = new HttpResponseMock(200);
        exception = new TwitterException("Test Exception", response);
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        
        response = new HttpResponseMock(400, "x-rate-limit-limit: 1000", "x-rate-limit-remaining: 500", "x-rate-limit-reset: 1577865600");
        exception = new TwitterException("Test Exception", response);
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(1000, exception.getFeatureSpecificRateLimitStatus().getLimit());
        assertEquals(500, exception.getFeatureSpecificRateLimitStatus().getRemaining());
        assertEquals(1577865600, exception.getFeatureSpecificRateLimitStatus().getResetTimeInSeconds());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test Exception");
        assertThrows(IllegalStateException.class, exception::getRetryAfter);
        
        HttpResponse response = new HttpResponseMock(200);
        exception = new TwitterException("Test Exception", response);
        assertThrows(IllegalStateException.class, exception::getRetryAfter);
        
        response = new HttpResponseMock(420, "Retry-After: 5");
        exception = new TwitterException("Test Exception", response);
        assertEquals(5, exception.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.isCausedByNetworkIssue());
        
        Exception nestedException = new IOException("Network Exception");
        exception = new TwitterException("Test Exception", nestedException);
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.exceededRateLimitation());
        
        HttpResponse response = new HttpResponseMock(200);
        exception = new TwitterException("Test Exception", response);
        assertFalse(exception.exceededRateLimitation());
        
        response = new HttpResponseMock(400, "x-rate-limit-limit: 1000", "x-rate-limit-remaining: 500", "x-rate-limit-reset: 1577865600");
        exception = new TwitterException("Test Exception", response);
        assertTrue(exception.exceededRateLimitation());
        
        response = new HttpResponseMock(420, "Retry-After: 5");
        exception = new TwitterException("Test Exception", response);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse response = new HttpResponseMock(200);
        exception = new TwitterException("Test Exception", response);
        assertFalse(exception.resourceNotFound());
        
        response = new HttpResponseMock(404);
        exception = new TwitterException("Test Exception", response);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("0x0000000000000000", exception.getExceptionCode());
        
        exception = new TwitterException("Test Exception", 500);
        assertEquals("0x0000000000000000", exception.getExceptionCode());
        
        exception = new TwitterException("Test Exception", new Exception("Nested Exception"), 400);
        assertEquals("0x0000000000000000", exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        assertEquals(exception1, exception2);
        
        HttpResponse response = new HttpResponseMock(400, "x-rate-limit-limit: 1000", "x-rate-limit-remaining: 500", "x-rate-limit-reset: 1577865600");
        exception1 = new TwitterException("Test Exception", response);
        exception2 = new TwitterException("Test Exception", response);
        assertEquals(exception1, exception2);
        
        exception2 = new TwitterException("Test Exception");
        assertNotEquals(exception1, exception2);
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        HttpResponse response = new HttpResponseMock(400, "x-rate-limit-limit: 1000", "x-rate-limit-remaining: 500", "x-rate-limit-reset: 1577865600");
        exception1 = new TwitterException("Test Exception 1", response);
        exception2 = new TwitterException("Test Exception 2", response);
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception2 = new TwitterException("Test Exception");
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("TwitterException{exceptionCode=[0x0000000000000000], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=Unknown}", exception.toString());
        
        HttpResponse response = new HttpResponseMock(400, "x-rate-limit-limit: 1000", "x-rate-limit-remaining: 500", "x-rate-limit-reset: 1577865600");
        exception = new TwitterException("Test Exception", response);
        assertEquals("TwitterException{exceptionCode=[0x0000000000000000], statusCode=400, retryAfter=0, rateLimitStatus=RateLimitStatus{limit=1000, remaining=500, resetTimeInSeconds=1577865600}, version=Unknown}", exception.toString());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.nested);
        
        exception.setNested();
        assertTrue(exception.nested);
    }
}

class HttpResponseMock implements HttpResponse {

    private int statusCode;
    private String[] headers;

    public HttpResponseMock(int statusCode, String... headers) {
        this.statusCode = statusCode;
        this.headers = headers;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getResponseHeader(String name) {
        for (String header : headers) {
            if (header.startsWith(name + ":")) {
                return header.split(":")[1].trim();
            }
        }
        return null;
    }
}