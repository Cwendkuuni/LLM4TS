import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionThrowsWhenGettingIoExceptionFromUncheckedIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.ioException();
    }

    @Test
    public void testNullIoExceptionGivenToConstructor() {
        Assert.assertNull(new UncheckedIOException(null));
    }

    @Test(expected = NullPointerException.class)
    public void testNullMessageGivenToConstructor() {
        IOException cause = new IOException("Test IOException");
        String message = null;
        try {
            UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, message);
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testEmptyMessageGivenToConstructor() {
        IOException cause = new IOException("Test IOException");
        String message = "";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, message);
        Assert.assertEquals("", uncheckedIOException.getMessage());
    }

    @Test
    public void testMessageGivenToConstructor() {
        IOException cause = new IOException("Test IOException");
        String message = "Test Message";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, message);
        Assert.assertEquals("Test Message", uncheckedIOException.getMessage());
    }

    @Test
    public void testSuppliedIoExceptionIsRetained() {
        IOException cause = new IOException("Test IOException");
        String message = "Test Message";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, message);
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }
}