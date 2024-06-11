import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("Unexpected exception message", ioException.getMessage(), uncheckedIOException.getMessage());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame("IOException instances should be the same", ioException, uncheckedIOException.ioException());
    }

    // Edge case 1: Test UncheckedIOException construction with null IOException
    @Test(expected = NullPointerException.class)
    public void testUncheckedIOExceptionNullIOException() {
        new UncheckedIOException(null);
    }

    // Edge case 2: Test UncheckedIOException construction with an exception other than IOException
    @Test(expected = IllegalArgumentException.class)
    public void testUncheckedIOExceptionNonIOException() {
        new UncheckedIOException(new RuntimeException());
    }

    // Exception handling: Test UncheckedIOException with no inner exception
    @Test
    public void testUncheckedIOExceptionNoInnerException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertNull("No inner exception expected", uncheckedIOException.ioException());
    }

    // Exception handling: Test UncheckedIOException with a null inner exception
    @Test(expected = NullPointerException.class)
    public void testUncheckedIOExceptionNullInnerException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        uncheckedIOException.ioException();
    }

    // Exception handling: Test UncheckedIOException with an inner exception of a different class
    @Test(expected = ClassCastException.class)
    public void testUncheckedIOExceptionDifferentClassException() {
        IllegalStateException illegalStateException = new IllegalStateException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(illegalStateException);
        uncheckedIOException.ioException();
    }

    // Exception handling: Test UncheckedIOException with an inner exception that is not an Exception
    @Test(expected = ClassCastException.class)
    public void testUncheckedIOExceptionNotAnException() {
        String message = "Test message";
        Object innerException = new Object();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(message, innerException);
        uncheckedIOException.ioException();
    }

}