import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TwitterExceptionTest {
    @Test
    public void testConstructorWithMsg() {
        TwitterException exception = new TwitterException("Test Message");
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
    }
    
    @Test
    public void testConstructorWithCause() {
        TwitterException causeException = new TwitterException("Cause Exception");
        TwitterException exception = new TwitterException(causeException);
        
        assertEquals("Cause Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertEquals(causeException, exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.nested);
    }
    
    @Test
    public void testConstructorWithMsgAndRes() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test Message", response);
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNull(exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testConstructorWithMsgAndCause() {
        TwitterException causeException = new TwitterException("Cause Exception");
        TwitterException exception = new TwitterException("Test Message", causeException);
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertEquals(causeException, exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
    }
    
    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        TwitterException causeException = new TwitterException("Cause Exception");
        TwitterException exception = new TwitterException("Test Message", causeException, 400);
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertEquals(causeException, exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Message");
        
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Message");
        
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Message");
        
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test Message");
        
        try {
            exception.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Message");
        
        assertFalse(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test Message");
        TwitterException exception2 = new TwitterException("Test Message", new HttpResponseImpl());
        
        assertFalse(exception1.exceededRateLimitation());
        assertFalse(exception2.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Message");
        
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Message");
        
        String exceptionCode = exception.getExceptionCode();
        
        for (String filter : TwitterException.FILTER) {
            assertFalse(exceptionCode.contains(filter));
        }
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.nested);
        
        exception.setNested();
        
        assertTrue(exception.nested);
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Message");
        TwitterException exception2 = new TwitterException("Test Message");
        TwitterException exception3 = new TwitterException("Test Message");
        exception1.rateLimitStatus = new RateLimitStatusJSONImpl();
        
        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Message");
        TwitterException exception2 = new TwitterException("Test Message");
        exception1.rateLimitStatus = new RateLimitStatusJSONImpl();
        
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Message");
        
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expected, exception.toString());
    }
    
    @Test
    public void testGetCause() {
        HttpResponse response1 = new HttpResponseImpl();
        response1.setStatusCode(304);
        HttpResponse response2 = new HttpResponseImpl();
        response2.setStatusCode(400);
        HttpResponse response3 = new HttpResponseImpl();
        response3.setStatusCode(401);
        HttpResponse response4 = new HttpResponseImpl();
        response4.setStatusCode(403);
        HttpResponse response5 = new HttpResponseImpl();
        response5.setStatusCode(404);
        HttpResponse response6 = new HttpResponseImpl();
        response6.setStatusCode(406);
        HttpResponse response7 = new HttpResponseImpl();
        response7.setStatusCode(420);
        HttpResponse response8 = new HttpResponseImpl();
        response8.setStatusCode(500);
        HttpResponse response9 = new HttpResponseImpl();
        response9.setStatusCode(502);
        HttpResponse response10 = new HttpResponseImpl();
        response10.setStatusCode(503);
        HttpResponse response11 = new HttpResponseImpl();
        response11.setStatusCode(600);
        
        String cause1 = TwitterException.getCause(response1);
        String cause2 = TwitterException.getCause(response2);
        String cause3 = TwitterException.getCause(response3);
        String cause4 = TwitterException.getCause(response4);
        String cause5 = TwitterException.getCause(response5);
        String cause6 = TwitterException.getCause(response6);
        String cause7 = TwitterException.getCause(response7);
        String cause8 = TwitterException.getCause(response8);
        String cause9 = TwitterException.getCause(response9);
        String cause10 = TwitterException.getCause(response10);
        String cause11 = TwitterException.getCause(response11);
        
        assertEquals("304:There was no new data to return.", cause1);
        assertEquals("400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", cause2);
        assertEquals("401:Authentication credentials were missing or incorrect.", cause3);
        assertEquals("403:The request is understood, but it has been refused.  An accompanying error message will explain why.", cause4);
        assertEquals("404:The URI requested is invalid or the resource requested, such as a user, does not exists.", cause5);
        assertEquals("406:Returned by the Search API when an invalid format is specified in the request.", cause6);
        assertEquals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", cause7);
        assertEquals("500:Something is broken.  Please post to the group so the Twitter team can investigate.", cause8);
        assertEquals("502:Twitter is down or being upgraded.", cause9);
        assertEquals("503:Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.", cause10);
        assertEquals("600:", cause11);
    }
}