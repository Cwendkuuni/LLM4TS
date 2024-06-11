import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
	
	@Test
	public void testConstructorWithStringMsg() {
		TwitterException exception = new TwitterException("Test Exception");
		assertEquals("Test Exception", exception.getMessage());
		assertEquals(-1, exception.getStatusCode());
		assertNull(exception.getRateLimitStatus());
		assertNull(exception.getFeatureSpecificRateLimitStatus());
	}

	@Test
	public void testConstructorWithExceptionCause() {
		Exception cause = new Exception("Test Cause");
		TwitterException exception = new TwitterException(cause);
		assertEquals("Test Cause", exception.getCause().getMessage());
		assertEquals(-1, exception.getStatusCode());
		assertNull(exception.getRateLimitStatus());
		assertNull(exception.getFeatureSpecificRateLimitStatus());
	}

	@Test
	public void testConstructorWithStringMsgAndHttpResponse() {
		HttpResponse res = new HttpResponse();
		res.setStatusCode(420);
		res.setResponseHeader("Retry-After", "60");
		TwitterException exception = new TwitterException("Test Exception", res);
		assertEquals("Test Exception\n420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
		assertEquals(420, exception.getStatusCode());
		assertEquals(60, exception.getRetryAfter());
		assertNotNull(exception.getRateLimitStatus());
		assertNotNull(exception.getFeatureSpecificRateLimitStatus());
	}

	@Test
	public void testConstructorWithStringMsgAndExceptionCause() {
		Exception cause = new Exception("Test Cause");
		TwitterException exception = new TwitterException("Test Exception", cause);
		assertEquals("Test Exception", exception.getMessage());
		assertEquals("Test Cause", exception.getCause().getMessage());
		assertEquals(-1, exception.getStatusCode());
		assertNull(exception.getRateLimitStatus());
		assertNull(exception.getFeatureSpecificRateLimitStatus());
	}

	@Test
	public void testConstructorWithStringMsgAndExceptionCauseAndStatusCode() {
		Exception cause = new Exception("Test Cause");
		TwitterException exception = new TwitterException("Test Exception", cause, 404);
		assertEquals("Test Exception", exception.getMessage());
		assertEquals("Test Cause", exception.getCause().getMessage());
		assertEquals(404, exception.getStatusCode());
		assertNull(exception.getRateLimitStatus());
		assertNull(exception.getFeatureSpecificRateLimitStatus());
	}

	@Test
	public void testGetRetryAfterWhenRateLimitNotExceeded() {
		TwitterException exception = new TwitterException("Test Exception");
		try {
			exception.getRetryAfter();
			fail("Expected IllegalStateException");
		} catch (IllegalStateException e) {
			assertEquals("Rate limitation is not exceeded", e.getMessage());
		}
	}

	@Test
	public void testIsCausedByNetworkIssue() {
		IOException ioException = new IOException("Test IO Exception");
		TwitterException exception = new TwitterException("Test Exception", ioException);
		assertTrue(exception.isCausedByNetworkIssue());
	}

	@Test
	public void testExceededRateLimitation() {
		TwitterException exception1 = new TwitterException("Test Exception1", null, 400);
		assertFalse(exception1.exceededRateLimitation());

		HttpResponse res = new HttpResponse();
		res.setStatusCode(400);
		res.setResponseHeader("Retry-After", "60");
		TwitterException exception2 = new TwitterException("Test Exception2", res);
		assertTrue(exception2.exceededRateLimitation());

		TwitterException exception3 = new TwitterException("Test Exception3", null, 420);
		assertTrue(exception3.exceededRateLimitation());
	}

	@Test
	public void testResourceNotFound() {
		TwitterException exception = new TwitterException("Test Exception", null, 404);
		assertTrue(exception.resourceNotFound());
	}

	@Test
	public void testGetExceptionCode() {
		TwitterException exception = new TwitterException("Test Exception");
		assertEquals("e3f080c69", exception.getExceptionCode());
	}

	@Test
	public void testSetNested() {
		TwitterException exception = new TwitterException("Test Exception");
		assertFalse(exception.nested);
		exception.setNested();
		assertTrue(exception.nested);
	}
	
	@Test
	public void testEquals() {
		TwitterException exception1 = new TwitterException("Test Exception");
		TwitterException exception2 = new TwitterException("Test Exception");
		assertEquals(exception1, exception2);
		
		Exception cause = new Exception("Test Cause");
		TwitterException exception3 = new TwitterException("Test Exception", cause);
		assertNotEquals(exception1, exception3);
	}
	
	@Test
	public void testHashCode() {
		TwitterException exception1 = new TwitterException("Test Exception");
		TwitterException exception2 = new TwitterException("Test Exception");
		assertEquals(exception1.hashCode(), exception2.hashCode());
		
		Exception cause = new Exception("Test Cause");
		TwitterException exception3 = new TwitterException("Test Exception", cause);
		assertNotEquals(exception1.hashCode(), exception3.hashCode());
	}
	
	@Test
	public void testToString() {
		TwitterException exception = new TwitterException("Test Exception");
		String expectedToString = "TwitterException{exceptionCode=[e3f080c69], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=4.0.7}";
		assertEquals(expectedToString, exception.toString());
	}
}