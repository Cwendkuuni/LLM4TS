package twitter4j;

import twitter4j.internal.http.*;
import java.io.*;
import org.junit.Test;
import org.junit.Assert;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertEquals("Test Exception", t.getMessage());
        Assert.assertEquals(-1, t.getStatusCode());
        Assert.assertNull(t.getRateLimitStatus());
        Assert.assertNull(t.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionException() {
        Exception e = new Exception("Test Exception");
        TwitterException t = new TwitterException(e);
        Assert.assertEquals(e, t.getCause());
        Assert.assertEquals(-1, t.getStatusCode());
        Assert.assertNull(t.getRateLimitStatus());
        Assert.assertNull(t.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionStringHttpResponse() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        TwitterException t = new TwitterException("Test Exception", res);
        Assert.assertEquals("Test Exception\n200:", t.getMessage());
        Assert.assertEquals(200, t.getStatusCode());
        Assert.assertNull(t.getRateLimitStatus());
        Assert.assertNull(t.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionStringException() {
        Exception e = new Exception("Test Exception");
        TwitterException t = new TwitterException("Test Exception", e);
        Assert.assertEquals("Test Exception", t.getMessage());
        Assert.assertEquals(e, t.getCause());
        Assert.assertEquals(-1, t.getStatusCode());
        Assert.assertNull(t.getRateLimitStatus());
        Assert.assertNull(t.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception e = new Exception("Test Exception");
        TwitterException t = new TwitterException("Test Exception", e, 200);
        Assert.assertEquals("Test Exception", t.getMessage());
        Assert.assertEquals(e, t.getCause());
        Assert.assertEquals(200, t.getStatusCode());
        Assert.assertNull(t.getRateLimitStatus());
        Assert.assertNull(t.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertEquals(-1, t.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertNull(t.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertNull(t.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException t = new TwitterException("Test Exception");
        try {
            t.getRetryAfter();
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertFalse(t.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertFalse(t.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertFalse(t.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertNotNull(t.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException t = new TwitterException("Test Exception");
        t.setNested();
        Assert.assertTrue(t.equals(t));
    }
    
    @Test
    public void testEquals() {
        TwitterException t1 = new TwitterException("Test Exception");
        TwitterException t2 = new TwitterException("Test Exception 2");
        Assert.assertFalse(t1.equals(null));
        Assert.assertFalse(t1.equals(new Object()));
        Assert.assertTrue(t1.equals(t1));
        Assert.assertFalse(t1.equals(t2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertNotNull(t.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException t = new TwitterException("Test Exception");
        Assert.assertNotNull(t.toString());
    }
    
    @Test
    public void testGetCause() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        Assert.assertNotNull(TwitterException.getCause(res));
    }
    
}