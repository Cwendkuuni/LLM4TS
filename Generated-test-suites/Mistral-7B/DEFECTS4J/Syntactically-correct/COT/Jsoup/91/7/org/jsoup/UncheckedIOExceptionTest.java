import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCause() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }

    // Edge cases and exception handling scenarios:
    // 1. Test with null IOException
    @Test(expected = NullPointerException.class)
    public void testUncheckedIOExceptionNullConstructor() {
        new UncheckedIOException(null);
    }

    // 2. Test getting cause from null UncheckedIOException
    @Test(expected = NullPointerException.class)
    public void testGetCauseNullUncheckedIOException() {
        UncheckedIOException uncheckedIOException = null;
        Assert.assertNull(uncheckedIOException.getCause());
    }

    // 3. Test getting ioException() from null UncheckedIOException
    @Test(expected = NullPointerException.class)
    public void testIoExceptionNullUncheckedIOException() {
        UncheckedIOException uncheckedIOException = null;
        Assert.assertNull(uncheckedIOException.ioException());
    }

    // 4. Test getting ioException() from UncheckedIOException with non-IOException cause
    @Test(expected = ClassCastException.class)
    public void testIoExceptionWithNonIOExceptionCause() {
        RuntimeException runtimeException = new RuntimeException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(runtimeException);
        Assert.assertNotSame(runtimeException, uncheckedIOException.ioException());
    }
}