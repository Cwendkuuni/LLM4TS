package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TwitterExceptionTest {
    @Test
    public void testTwitterExceptionString() {
        TwitterException twitterException = new TwitterException("Test");
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception("Cause");
        TwitterException twitterException = new TwitterException(cause);
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testTwitterExceptionStringHttpResponse() {
        HttpResponse httpResponse = new HttpResponseImpl(200, "OK", "");
        TwitterException twitterException = new TwitterException("Test", httpResponse);
        assertEquals(200, twitterException.getStatusCode());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionStringException() {
        Exception cause = new Exception("Cause");
        TwitterException twitterException = new TwitterException("Test", cause);
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertFalse(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception cause = new Exception("Cause");
        TwitterException twitterException = new TwitterException("Test", cause, 500);
        assertEquals(500, twitterException.getStatusCode());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        HttpResponse httpResponse = new HttpResponseImpl(420, "Rate Limited", "");
        TwitterException twitterException = new TwitterException("Test", httpResponse);
        assertEquals(420, twitterException.getStatusCode());
        assertEquals(-1, twitterException.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        HttpResponse httpResponse = new HttpResponseImpl(200, "OK", "");
        TwitterException twitterException = new TwitterException("Test", httpResponse);
        assertFalse(twitterException.isCausedByNetworkIssue());
        
        Exception cause = new IOException("Network Issue");
        twitterException = new TwitterException(cause);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponseImpl(400, "Bad Request", "");
        TwitterException twitterException = new TwitterException("Test", httpResponse);
        assertTrue(twitterException.exceededRateLimitation());
        
        httpResponse = new HttpResponseImpl(420, "Rate Limited", "");
        twitterException = new TwitterException("Test", httpResponse);
        assertTrue(twitterException.exceededRateLimitation());
        
        httpResponse = new HttpResponseImpl(200, "OK", "");
        twitterException = new TwitterException("Test", httpResponse);
        assertFalse(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponseImpl(404, "Not Found", "");
        TwitterException twitterException = new TwitterException("Test", httpResponse);
        assertTrue(twitterException.resourceNotFound());
        
        httpResponse = new HttpResponseImpl(200, "OK", "");
        twitterException = new TwitterException("Test", httpResponse);
        assertFalse(twitterException.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        HttpResponse httpResponse = new HttpResponseImpl(400, "Bad Request", "");
        TwitterException twitterException = new TwitterException("Test", httpResponse);
        assertEquals("400:twitter4j", twitterException.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        Exception cause1 = new Exception("Cause");
        Exception cause2 = new Exception("Cause");
        
        TwitterException twitterException1 = new TwitterException("Test", cause1, 500);
        TwitterException twitterException2 = new TwitterException("Test", cause2, 500);
        assertTrue(twitterException1.equals(twitterException2));
        
        twitterException1 = new TwitterException("Test", cause1, 500);
        twitterException2 = new TwitterException("Test", cause2, 200);
        assertFalse(twitterException1.equals(twitterException2));
    }
    
    @Test
    public void testHashCode() {
        Exception cause = new Exception("Cause");
        
        TwitterException twitterException = new TwitterException("Test", cause, 500);
        assertEquals(79085, twitterException.hashCode());
    }
    
    @Test
    public void testToString() {
        HttpResponse httpResponse = new HttpResponseImpl(200, "OK", "");
        TwitterException twitterException = new TwitterException("Test", httpResponse);
        assertEquals("TwitterException{exceptionCode=[200:TwitterException{exceptionCode=[200:34a375a0], }], statusCode=200, retryAfter=0, rateLimitStatus=null, version=4.0.7-SNAPSHOT}", twitterException.toString());
    }
}