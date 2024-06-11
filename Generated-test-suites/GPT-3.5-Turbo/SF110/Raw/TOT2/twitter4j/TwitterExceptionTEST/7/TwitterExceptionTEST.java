import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMsg() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMsgAndRes() {
        HttpResponse res = new HttpResponseImpl();
        TwitterException exception = new TwitterException("Test Message", res);
        assertEquals(res.getStatusCode(), exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }

    // Add more test cases for other public methods

}