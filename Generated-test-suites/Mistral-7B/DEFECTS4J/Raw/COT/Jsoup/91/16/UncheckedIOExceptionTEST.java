import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertEquals("Test IOException", uncheckedIOException.getMessage());
        assertEquals(IOException.class, uncheckedIOException.getClass());
        assertEquals(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        IOException actualIOException = uncheckedIOException.ioException();
        assertEquals(cause, actualIOException);
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowsUncheckedIOException() throws IOException {
        IOException cause = new IOException("Test IOException");
        throw new UncheckedIOException(cause);
    }

    @Test(expected = IOException.class)
    public void testThrowsWrappedIOException() throws IOException {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testNullCause() {
        try {
            new UncheckedIOException(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyMessageCause() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertNotNull(uncheckedIOException.getMessage());
    }
}