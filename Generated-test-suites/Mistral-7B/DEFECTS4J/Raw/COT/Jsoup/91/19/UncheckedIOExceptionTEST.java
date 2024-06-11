import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        Assert.assertEquals("Test IOException", uncheckedIOException.ioException().getMessage());
    }

    @Test(expected = IOException.class)
    public void testUncheckedIOExceptionThrowsWrappedIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testNullIOExceptionArgument() {
        Assert.assertNull(new UncheckedIOException(null));
    }

    @Test
    public void testEmptyIOExceptionMessage() {
        IOException cause = new IOException("");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("", uncheckedIOException.getMessage());
    }

    @Test
    public void testUncheckedIOExceptionWithCustomMessage() {
        IOException cause = new IOException("Test IOException");
        String customMessage = "Custom Error Message";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, customMessage);
        Assert.assertEquals("Custom Error Message", uncheckedIOException.getMessage());
        Assert.assertEquals("Test IOException", uncheckedIOException.getCause().getMessage());
    }
}