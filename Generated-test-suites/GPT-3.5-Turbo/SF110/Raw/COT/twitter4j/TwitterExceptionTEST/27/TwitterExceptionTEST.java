import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    private TwitterException twitterException;

    @Before
    public void setUp() {
        twitterException = new TwitterException("");
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(-1, twitterException.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter_NotExceededRateLimitation() {
        try {
            twitterException.getRetryAfter();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        assertFalse(twitterException.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        assertEquals("", twitterException.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException otherException = new TwitterException("");
        assertTrue(twitterException.equals(otherException));
    }

    @Test
    public void testHashCode() {
        assertEquals(-1, twitterException.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expected, twitterException.toString());
    }
}