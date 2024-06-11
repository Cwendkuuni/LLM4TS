import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
  
  private TwitterException twitterException;
  
  @Before
  public void setUp() {
    twitterException = new TwitterException("Test Exception");
  }

  @Test
  public void testConstructorWithMessage() {
    String expectedMessage = "Test Exception";
    assertEquals(expectedMessage, twitterException.getMessage());
    assertEquals(-1, twitterException.getStatusCode());
    assertNull(twitterException.getRateLimitStatus());
    assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertFalse(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
  }

  @Test
  public void testConstructorWithCause() {
    Exception cause = new Exception("Test Cause");
    twitterException = new TwitterException(cause);
    assertEquals(null, twitterException.getMessage());
    assertEquals(-1, twitterException.getStatusCode());
    assertNull(twitterException.getRateLimitStatus());
    assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertFalse(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
  }

  @Test
  public void testConstructorWithMessageAndResponse() {
    HttpResponse response = new HttpResponseImpl();
    response.setStatusCode(420);
    response.setResponseHeader("Retry-After", "60");
    String expectedMessage = "420: The number of requests you have made exceeds the quota afforded by your assigned rate limit.\nTest Exception";
    twitterException = new TwitterException("Test Exception", response);
    assertEquals(expectedMessage, twitterException.getMessage());
    assertEquals(420, twitterException.getStatusCode());
    assertNotNull(twitterException.getRateLimitStatus());
    assertNotNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertEquals(60, twitterException.getRetryAfter());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertTrue(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
  }

  @Test
  public void testConstructorWithMessageAndCause() {
    Exception cause = new Exception("Test Cause");
    twitterException = new TwitterException("Test Exception", cause);
    assertEquals("Test Exception", twitterException.getMessage());
    assertEquals(-1, twitterException.getStatusCode());
    assertNull(twitterException.getRateLimitStatus());
    assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertFalse(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
  }

  @Test
  public void testConstructorWithMessageCauseAndStatusCode() {
    Exception cause = new Exception("Test Cause");
    twitterException = new TwitterException("Test Exception", cause, 500);
    assertEquals("Test Exception", twitterException.getMessage());
    assertEquals(500, twitterException.getStatusCode());
    assertNull(twitterException.getRateLimitStatus());
    assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertFalse(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
  }

  @Test
  public void testGetExceptionCode() {
    twitterException = new TwitterException("Test Exception");
    String expectedExceptionCode = "171f2b791364fcba";
    assertEquals(expectedExceptionCode, twitterException.getExceptionCode());
  }

  @Test
  public void testEquals() {
    twitterException = new TwitterException("Test Exception");
    TwitterException sameException = new TwitterException("Test Exception");
    TwitterException differentException = new TwitterException("Different Exception");

    assertTrue(twitterException.equals(sameException));
    assertFalse(twitterException.equals(differentException));
  }

  @Test
  public void testHashCode() {
    twitterException = new TwitterException("Test Exception");
    TwitterException sameException = new TwitterException("Test Exception");

    assertEquals(twitterException.hashCode(), sameException.hashCode());
  }

  @Test
  public void testToString() {
    twitterException = new TwitterException("Test Exception");
    String expectedString = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
    assertEquals(expectedString, twitterException.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetRetryAfterWithNonRateLimitationStatusCode() {
    twitterException = new TwitterException("Test Exception");
    twitterException.getRetryAfter();
  }

  @Test
  public void testIsCausedByNetworkIssue() {
    twitterException = new TwitterException(new IOException());
    assertTrue(twitterException.isCausedByNetworkIssue());
  }

  @Test
  public void testExceededRateLimitation() {
    twitterException = new TwitterException("Test Exception");
    assertFalse(twitterException.exceededRateLimitation());

    HttpResponse response1 = new HttpResponseImpl();
    twitterException = new TwitterException("Test Exception", response1);
    assertFalse(twitterException.exceededRateLimitation());

    HttpResponse response2 = new HttpResponseImpl();
    response2.setStatusCode(400);
    twitterException = new TwitterException("Test Exception", response2);
    assertTrue(twitterException.exceededRateLimitation());

    HttpResponse response3 = new HttpResponseImpl();
    response3.setStatusCode(420);
    twitterException = new TwitterException("Test Exception", response3);
    assertTrue(twitterException.exceededRateLimitation());
  }

  @Test
  public void testResourceNotFound() {
    twitterException = new TwitterException("Test Exception");
    assertFalse(twitterException.resourceNotFound());

    HttpResponse response = new HttpResponseImpl();
    response.setStatusCode(404);
    twitterException = new TwitterException("Test Exception", response);
    assertTrue(twitterException.resourceNotFound());
  }
  
  // additional test cases can be added for other methods if required

}