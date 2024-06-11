package twitter4j;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterExceptionTest {

    private TwitterException twitterException;

    @Before
    public void setUp() {
        twitterException = new TwitterException("Test exception");
    }

    @Test
    public void testConstructor1() {
        assertEquals("Test exception", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor2() {
        Exception cause = new Exception("Test cause");
        TwitterException twitterException = new TwitterException(cause);
        assertSame(cause, twitterException.getCause());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor3() {
        HttpResponse res = new HttpResponse() {
            @Override
            public RateLimitStatus getRateLimitStatus() {
                return null;
            }

            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                return "60";
            }
        };
        TwitterException twitterException = new TwitterException("Test exception", res);
        assertEquals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.\n" +
                "Test exception", twitterException.getMessage());
        assertEquals(420, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertEquals(60, twitterException.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWithoutRateLimitExceeded() {
        twitterException.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(twitterException.isCausedByNetworkIssue());
        twitterException = new TwitterException(new IOException());
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        assertFalse(twitterException.exceededRateLimitation());
        twitterException = new TwitterException("Test exception", new ErrorResponse(400), 400);
        assertTrue(twitterException.exceededRateLimitation());
        twitterException = new TwitterException("Test exception", new ErrorResponse(420), 420);
        assertTrue(twitterException.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        assertFalse(twitterException.resourceNotFound());
        twitterException = new TwitterException("Test exception", new ErrorResponse(404), 404);
        assertTrue(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        twitterException = new TwitterException("Test exception", new ErrorResponse(400), 400);
        String expectedExceptionCode = new ExceptionDiagnosis(twitterException, TwitterException.FILTER).asHexString();
        assertEquals(expectedExceptionCode, twitterException.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test exception", new ErrorResponse(400), 400);
        TwitterException twitterException2 = new TwitterException("Test exception", new ErrorResponse(400), 400);
        assertTrue(twitterException1.equals(twitterException2));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException1 = new TwitterException("Test exception", new ErrorResponse(400), 400);
        TwitterException twitterException2 = new TwitterException("Test exception", new ErrorResponse(400), 400);
        assertEquals(twitterException1.hashCode(), twitterException2.hashCode());
    }

    @Test
    public void testToString() {
        twitterException = new TwitterException("Test exception", new ErrorResponse(400), 400);
        String expectedToString = "TwitterException{exceptionCode=[" + twitterException.getExceptionCode() + "], " +
                "statusCode=" + twitterException.getStatusCode() + ", retryAfter=0, " +
                "rateLimitStatus=null, version=" + Version.getVersion() + '}';
        assertEquals(expectedToString, twitterException.toString());
    }
}