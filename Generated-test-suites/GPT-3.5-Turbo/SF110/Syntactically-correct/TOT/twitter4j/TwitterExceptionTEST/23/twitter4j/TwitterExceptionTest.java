import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TwitterExceptionTest {
    
    private TwitterException exception;
    
    @Before
    public void setUp() {
        exception = new TwitterException("Test Exception");
    }
    
    @Test
    public void testConstructorWithMsg() {
        Assert.assertEquals("Test Exception", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException twitterException = new TwitterException(cause);
        
        Assert.assertEquals("java.lang.Exception: Test Cause", twitterException.getMessage());
        Assert.assertEquals(-1, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testConstructorWithMsgAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(404);
        httpResponse.setResponseHeader("Retry-After", "60");
        
        TwitterException twitterException = new TwitterException("Test Message", httpResponse);
        
        Assert.assertEquals("404:The URI requested is invalid or the resource requested, such as a user, does not exists.\nTest Message", twitterException.getMessage());
        Assert.assertEquals(404, twitterException.getStatusCode());
        Assert.assertEquals(60, twitterException.getRetryAfter());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testConstructorWithMsgAndCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException twitterException = new TwitterException("Test Message", cause);
        
        Assert.assertEquals("Test Message", twitterException.getMessage());
        Assert.assertEquals(-1, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        Exception cause = new Exception("Test Cause");
        TwitterException twitterException = new TwitterException("Test Message", cause, 500);
        
        Assert.assertEquals("Test Message", twitterException.getMessage());
        Assert.assertEquals(500, twitterException.getStatusCode());
        Assert.assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testGetRateLimitStatus_ExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(400);
        
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        
        TwitterException twitterException = new TwitterException("Test Message", httpResponse);
        twitterException.rateLimitStatus = rateLimitStatus;
        
        Assert.assertEquals(rateLimitStatus, twitterException.getRateLimitStatus());
        Assert.assertTrue(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testGetRateLimitStatus_NotExceededRateLimitation() {
        TwitterException twitterException = new TwitterException("Test Message");
        
        Assert.assertNull(twitterException.getRateLimitStatus());
        Assert.assertFalse(twitterException.exceededRateLimitation());
    }
    
 	@Test(expected = IllegalStateException.class)
	public void testGetRetryAfter_NotExceededRateLimitation() {
		TwitterException twitterException = new TwitterException("Test Message");
		
		twitterException.getRetryAfter();
	}
	
	@Test
	public void testEquals_SameObject() {
		TwitterException twitterException = new TwitterException("Test Message");
		
		Assert.assertTrue(twitterException.equals(twitterException));
	}
	
	@Test
	public void testEquals_NullObject() {
		TwitterException twitterException = new TwitterException("Test Message");
		
		Assert.assertFalse(twitterException.equals(null));
	}
	
	@Test
	public void testEquals_ObjectsWithDifferentRetryAfter() {
		TwitterException twitterException1 = new TwitterException("Test Message");
		twitterException1.retryAfter = 60;
		
		TwitterException twitterException2 = new TwitterException("Test Message");
		twitterException2.retryAfter = 30;
		
		Assert.assertFalse(twitterException1.equals(twitterException2));
	}
	
	@Test
	public void testEquals_ObjectsWithDifferentStatusCode() {
		TwitterException twitterException1 = new TwitterException("Test Message");
		twitterException1.statusCode = 200;
		
		TwitterException twitterException2 = new TwitterException("Test Message");
		twitterException2.statusCode = 500;
		
		Assert.assertFalse(twitterException1.equals(twitterException2));
	}
	
	@Test
	public void testEquals_ObjectsWithDifferentRateLimitStatus() {
		RateLimitStatus rateLimitStatus1 = new RateLimitStatusJSONImpl();
		RateLimitStatus rateLimitStatus2 = new RateLimitStatusJSONImpl();
		rateLimitStatus1.setRateLimitStatus("limit1", 1, 1);
		
		TwitterException twitterException1 = new TwitterException("Test Message");
		twitterException1.rateLimitStatus = rateLimitStatus1;
		
		TwitterException twitterException2 = new TwitterException("Test Message");
		twitterException2.rateLimitStatus = rateLimitStatus2;
		
		Assert.assertFalse(twitterException1.equals(twitterException2));
	}
	
	@Test
	public void testEquals_ObjectsWithSameProperties() {
		RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
		rateLimitStatus.setRateLimitStatus("limit", 10, 10);
		
		TwitterException twitterException1 = new TwitterException("Test Message");
		twitterException1.rateLimitStatus = rateLimitStatus;
		
		TwitterException twitterException2 = new TwitterException("Test Message");
		twitterException2.rateLimitStatus = rateLimitStatus;
		
		Assert.assertTrue(twitterException1.equals(twitterException2));
	}
	
	@Test
	public void testHashCode() {
		RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
		rateLimitStatus.setRateLimitStatus("limit", 10, 10);

		TwitterException twitterException = new TwitterException("Test Message");
		twitterException.rateLimitStatus = rateLimitStatus;
		
		int expectedHashCode = twitterException.getStatusCode() + 31 * twitterException.getRetryAfter() + ((twitterException.getRateLimitStatus() != null) ? twitterException.getRateLimitStatus().hashCode() : 0);
		
		Assert.assertEquals(expectedHashCode, twitterException.hashCode());
	}
	
	@Test
	public void testToString() {
		RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
		rateLimitStatus.setRateLimitStatus("limit", 10, 10);
		
		TwitterException twitterException = new TwitterException("Test Message");
		twitterException.rateLimitStatus = rateLimitStatus;
		
		String expectedString = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=-1, rateLimitStatus=" + rateLimitStatus + ", version=N/A}";
		
		Assert.assertEquals(expectedString, twitterException.toString());
	}
    
}