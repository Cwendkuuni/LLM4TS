import org.junit.Test;
import org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentMatchers;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = mock(IOException.class);
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionGetting() {
        IOException ioException = new IOException("Test IoException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowUncheckedIOException() {
        IOException ioException = new IOException("Test IoException");
        throw new UncheckedIOException(ioException);
    }

    @Test
    public void testNullIoException() {
        try {
            new UncheckedIOException(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyConstructor() {
        try {
            new UncheckedIOException();
            fail("Should not be able to instantiate UncheckedIOException without an IOException cause");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testNullMessage() {
        IOException ioException = new IOException("Test IoException");
        try {
            new UncheckedIOException(ioException, null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testUncheckedIOExceptionWithMessage() {
        IOException ioException = new IOException("Test IoException");
        String message = "Test UncheckedIOExceptionWithMessage";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException, message);
        assertEquals(message, uncheckedIOException.getMessage());
        assertEquals(ioException, uncheckedIOException.ioException());
    }

}