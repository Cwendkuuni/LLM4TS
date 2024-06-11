import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    // Edge case 1: Null IOException cause
    @Test(expected = NullPointerException.class)
    public void testNullIOExceptionCause() {
        new UncheckedIOException(null);
    }

    // Edge case 2: Empty String message
    @Test
    public void testEmptyStringMessage() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("", uncheckedIOException.getMessage());
    }

    // Exception handling: IOException with message
    @Test
    public void testIoeWithMessageInUncheckedIOException() {
        IOException cause = new IOException("Test I/O Exception with message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Test I/O Exception with message", uncheckedIOException.getMessage());
    }

    // Exception handling: IOException without message
    @Test
    public void testIoeWithoutMessageInUncheckedIOException() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("java.io.IOException", uncheckedIOException.getMessage());
    }

    // Exception handling: Multiple exceptions
    @Test(expected = IOException.class)
    public void testMultipleExceptionsInUncheckedIOException() {
        IOException ioException1 = new IOException("Test IOException 1");
        IOException ioException2 = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException1, ioException2);
        throw uncheckedIOException.ioException();
    }

    // Exception handling: Wrapped exception of different class
    @Test(expected = RuntimeException.class)
    public void testWrappedExceptionOfDifferentClassInUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        Exception wrappedException = new IllegalArgumentException("Test IllegalArgumentException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException, wrappedException);
        throw uncheckedIOException;
    }

    // Exception handling: No wrapped exception
    @Test
    public void testNoWrappedExceptionInUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertNull(uncheckedIOException.getCause());
    }

}