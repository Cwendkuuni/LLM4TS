package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class TwitterExceptionTest {
  
  @Test
  public void testConstructorWithMsg() {

    TwitterException ex = new TwitterException("Test Message");
    
    assertEquals("Test Message", ex.getMessage());
    assertEquals(-1, ex.getStatusCode());
    assertNull(ex.getRateLimitStatus());
    assertNull(ex.getFeatureSpecificRateLimitStatus());
    assertFalse(ex.isCausedByNetworkIssue());
    assertFalse(ex.exceededRateLimitation());
    assertFalse(ex.resourceNotFound());

  }
  
  @Test
  public void testConstructorWithCause() {

    Exception cause = new Exception("Test Cause");
    
    TwitterException ex = new TwitterException(cause);
    
    assertSame(cause, ex.getCause());
    assertEquals(-1, ex.getStatusCode());
    assertNull(ex.getRateLimitStatus());
    assertNull(ex.getFeatureSpecificRateLimitStatus());
    assertFalse(ex.isCausedByNetworkIssue());
    assertFalse(ex.exceededRateLimitation());
    assertFalse(ex.resourceNotFound());

  }
  
  @Test
  public void testConstructorWithMsgAndRes() {

    HttpResponse res = new HttpResponse();
    res.setStatusCode(400);
    res.setResponseHeader("Retry-After", "30");
    
    TwitterException ex = new TwitterException("Test Message", res);
    
    assertEquals("Test Message", ex.getMessage());
    assertEquals(400, ex.getStatusCode());
    assertNotNull(ex.getRateLimitStatus());
    assertNull(ex.getFeatureSpecificRateLimitStatus());
    assertFalse(ex.isCausedByNetworkIssue());
    assertTrue(ex.exceededRateLimitation());
    assertFalse(ex.resourceNotFound());

  }
  
  @Test
  public void testConstructorWithMsgAndCause() {

    Exception cause = new Exception("Test Cause");
    
    TwitterException ex = new TwitterException("Test Message", cause);
    
    assertEquals("Test Message", ex.getMessage());
    assertSame(cause, ex.getCause());
    assertEquals(-1, ex.getStatusCode());
    assertNull(ex.getRateLimitStatus());
    assertNull(ex.getFeatureSpecificRateLimitStatus());
    assertFalse(ex.isCausedByNetworkIssue());
    assertFalse(ex.exceededRateLimitation());
    assertFalse(ex.resourceNotFound());

  }
  
  @Test
  public void testConstructorWithMsgAndCauseAndStatusCode() {

    Exception cause = new Exception("Test Cause");
    
    TwitterException ex = new TwitterException("Test Message", cause, 400);
    
    assertEquals("Test Message", ex.getMessage());
    assertSame(cause, ex.getCause());
    assertEquals(400, ex.getStatusCode());
    assertNull(ex.getRateLimitStatus());
    assertNull(ex.getFeatureSpecificRateLimitStatus());
    assertFalse(ex.isCausedByNetworkIssue());
    assertFalse(ex.exceededRateLimitation());
    assertFalse(ex.resourceNotFound());

  }
  
  @Test
  public void testGetRetryAfter() {
    
    HttpResponse res = new HttpResponse();
    res.setStatusCode(420);
    res.setResponseHeader("Retry-After", "30");
    
    TwitterException ex = new TwitterException("Test Message", res);
    
    assertEquals(30, ex.getRetryAfter());
  }
  
  @Test
  public void testGetRetryAfterWhenStatusCodeNot420() {
    
    HttpResponse res = new HttpResponse();
    res.setStatusCode(400);
    
    TwitterException ex = new TwitterException("Test Message", res);
    
    try {
      ex.getRetryAfter();
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
  
  @Test
  public void testEquals() {
    
    TwitterException ex1 = new TwitterException("Test Message");
    TwitterException ex2 = new TwitterException("Test Message");
    TwitterException ex3 = new TwitterException("Different Message");
    
    assertTrue(ex1.equals(ex2));
    assertFalse(ex1.equals(ex3));
  }
  
  @Test
  public void testHashCode() {
    
    TwitterException ex1 = new TwitterException("Test Message");
    TwitterException ex2 = new TwitterException("Test Message");
    TwitterException ex3 = new TwitterException("Different Message");
    
    assertEquals(ex1.hashCode(), ex2.hashCode());
    assertNotEquals(ex1.hashCode(), ex3.hashCode());
  }
  
  @Test
  public void testToString() {
    
    TwitterException ex = new TwitterException("Test Message");
    
    String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
    
    assertEquals(expected, ex.toString());
  }

}