import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test(expected = NullPointerException.class)
    public void testUncheckedIOExceptionWithNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    }

    @Test
    public void testUncheckedIOExceptionWithCustomCause() {
        IOException customCause = new EOFException("Custom Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(customCause);

        assertEquals(customCause, uncheckedIOException.getCause());
    }
}