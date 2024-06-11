import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertFalse("Unexpected null exception cause", uncheckedIOException.getCause() == null);
        Assert.assertEquals("IOException cause should be the same", cause, uncheckedIOException.getCause());
        Assert.assertEquals("UncheckedIOException message should be the same as IOException message", cause.getMessage(), uncheckedIOException.getMessage());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertInstanceOf("ioException() should return an IOException", IOException.class, uncheckedIOException.ioException());
    }

    // Edge cases and exception handling tests

    @Test(expected = NullPointerException.class)
    public void testNullIOExceptionConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    }

    @Test
    public void testEmptyStringIoExceptionMessage() {
        IOException cause = new IOException("");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Empty string message should not be changed", "", uncheckedIOException.getMessage());
    }

    @Test
    public void testUncheckedIOExceptionWithCustomMessage() {
        IOException cause = new IOException("Test IOException");
        String customMessage = "Custom UncheckedIOException message";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, customMessage);
        Assert.assertEquals("Custom message should be the same", customMessage, uncheckedIOException.getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUncheckedIOExceptionWithNullCustomMessage() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, null);
    }

    @Test
    public void testToStringMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expectedToString = "UncheckedIOException: Test IOException";
        String actualToString = uncheckedIOException.toString();
        Assert.assertEquals("toString() should return the expected string", expectedToString, actualToString);
    }
}