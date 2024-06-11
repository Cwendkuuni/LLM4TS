import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*; // For assertions

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("io exception", ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetIoException() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        IOException actualIoException = uncheckedIOException.ioException();
        assertEquals("io exception", ioException, actualIoException);
    }

    @Test
    public void testUncheckedIOExceptionThrowsCorrectException() {
        IOException ioException = new IOException("Something went wrong");
        UncheckedIOException uncheckedIOException = null;
        try {
            uncheckedIOException = new UncheckedIOException(ioException);
        } catch (RuntimeException e) {
            // Expected exception
            assertEquals("IOException: Something went wrong", ioException.getMessage(), e.getCause().getMessage());
        }
    }

    @Test
    public void testUncheckedIOExceptionDoesNotWrapCheckedException() {
        IOException ioException = new IOException("Something went wrong");
        try {
            UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
            fail("Expected UncheckedIOException but got IOException");
        } catch (UncheckedIOException e) {
            // Expected exception
        }
    }
}