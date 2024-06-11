package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.mockito.Mockito;

import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        String msg = "Test Message";
        TwitterException exception = new TwitterException(msg);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = Mockito.mock(Exception.class);
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        String msg = "Test Message";
        HttpResponse response = Mockito.mock(HttpResponse.class);
        Mockito.when(response.getStatusCode()).thenReturn(200);
        Mockito.when(response.getResponseHeader(Mockito.anyString())).thenReturn(null);
        TwitterException exception = new TwitterException(msg, response);
        assertEquals(msg, exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getCause());
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringException() {
        String msg = "Test Message";
        Exception cause = Mockito.mock(Exception.class);
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        String msg = "Test Message";
        Exception cause = Mockito.mock(Exception.class);
        int statusCode = 200;
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        int statusCode = 200;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        TwitterException exception = new TwitterException("");
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = Mockito.mock(RateLimitStatus.class);
        TwitterException exception = new TwitterException("");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        int retryAfter = 30;
        TwitterException exception = new TwitterException("");
        exception.retryAfter = retryAfter;
        assertEquals(retryAfter, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("");
        assertFalse(exception.isCausedByNetworkIssue());

        IOException cause = Mockito.mock(IOException.class);
        exception = new TwitterException(cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        exception.rateLimitStatus = rateLimitStatus;
        assertTrue(exception.exceededRateLimitation());

        statusCode = 200;
        exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        int statusCode = 404;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertTrue(exception.resourceNotFound());

        statusCode = 200;
        exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("");
        String exceptionCode = "exception_code";
        ExceptionDiagnosis diagnosis = Mockito.mock(ExceptionDiagnosis.class);
        Mockito.when(diagnosis.asHexString()).thenReturn(exceptionCode);
        exception.FILTER = new String[] { "twitter4j" };
        exception = Mockito.spy(exception);
        Mockito.doReturn(diagnosis).when(exception).new ExceptionDiagnosis(Mockito.any(), Mockito.any());
        assertEquals(exceptionCode, exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("");
        assertFalse(exception.nested);
        exception.setNested();
        assertTrue(exception.nested);
    }

    @Test
    public void testEquals() {
        TwitterException exception = new TwitterException("");
        assertFalse(exception.equals(null));
        assertFalse(exception.equals(new Object()));

        TwitterException exception1 = new TwitterException("");
        assertTrue(exception.equals(exception1));

        TwitterException exception2 = new TwitterException("");
        exception2.statusCode = 200;
        assertFalse(exception1.equals(exception2));

        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        exception1.rateLimitStatus = rateLimitStatus;
        assertFalse(exception1.equals(exception2));

        exception2.rateLimitStatus = rateLimitStatus;
        assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("");
        int hashCode = 31 * (31 * (31 * (31 * 1 + 0) + 0) + 0) + 0;
        assertEquals(hashCode, exception.hashCode());

        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        exception.rateLimitStatus = rateLimitStatus;

        hashCode = 31 * (31 * (31 * (31 * 1 + 0) + 0) + rateLimitStatus.hashCode()) + 0;
        assertEquals(hashCode, exception.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("");
        String exceptionCode = "exception_code";
        ExceptionDiagnosis diagnosis = Mockito.mock(ExceptionDiagnosis.class);
        Mockito.when(diagnosis.asHexString()).thenReturn(exceptionCode);
        exception.FILTER = new String[] { "twitter4j" };
        exception.rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        exception.retryAfter = 30;
        exception.statusCode = 200;
        String version = "1.0.0";
        Version.setVersion(version);
        exception = Mockito.spy(exception);
        Mockito.doReturn(diagnosis).when(exception).new ExceptionDiagnosis(Mockito.any(), Mockito.any());
        String expectedString = "TwitterException{" + "exceptionCode=[" + exceptionCode + "], " + "statusCode="
                + exception.statusCode + ", " + "retryAfter=" + exception.retryAfter + ", " + "rateLimitStatus="
                + exception.rateLimitStatus + ", " + "version=" + version + '}';
        assertEquals(expectedString, exception.toString());
    }

    private class ExceptionDiagnosis {

        public ExceptionDiagnosis(TwitterException twitterException, String[] filter) {
        }

        public String asHexString() {
            return "";
        }
    }

    private static class Version {
        public static void setVersion(String version) {
        }

        public static String getVersion() {
            return "";
        }
    }
}