import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionconstruction() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.instanceOf(IOException.class, uncheckedIOException.ioException());
    }

    // Edge case 1: Test with null IOException
    @Test(expected = NullPointerException.class)
    public void testUncheckedIOExceptionNullIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    }

    // Edge case 2: Test empty String as IOException message
    @Test
    public void testUncheckedIOExceptionEmptyMessageIOException() {
        IOException cause = new IOException("");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("", uncheckedIOException.getMessage());
    }

    // Edge case 3: Test with custom message and IOException
    @Test
    public void testUncheckedIOExceptionCustomMessageAndIOException() {
        String customMessage = "Test Custom Message";
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(customMessage, cause);
        Assert.assertEquals(customMessage, uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    // Exception handling scenario 1: Test IllegalArgumentException when passing non-IOException object
    @Test(expected = IllegalArgumentException.class)
    public void testUncheckedIOExceptionIllegalArgumentException() {
        Object nonIOExceptionObject = new Object();
        UncheckedIOException uncheckedIOException = new UncheckedIOException((IOException) nonIOExceptionObject);
    }

    // Exception handling scenario 2: Test ClassCastException when the cause is not an IOException
    @Test(expected = ClassCastException.class)
    public void testUncheckedIOExceptionClassCastException() {
        RuntimeException cause = new RuntimeException("Test RuntimeException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
    }
}