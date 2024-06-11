import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertInstanceof(UncheckedIOException.class, uncheckedIOException);
        assertInstanceof(IOException.class, uncheckedIOException.ioException());
        assertEquals("Test IOException", uncheckedIOException.getCause().getMessage());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        IOException actualIoException = uncheckedIOException.ioException();
        assertSame(cause, actualIoException);
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowUncheckedIOException() {
        try {
            throw new UncheckedIOException(new IOException("Test IOException"));
        } catch (IOException e) {
            fail("Expected UncheckedIOException, but got IOException: " + e.getMessage());
        }
    }

    @Test(expected = UncheckedIOException.class)
    public void testUncheckedIOExceptionThrowsExpectedException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        try {
            throw uncheckedIOException;
        } catch (IOException e) {
            fail("Expected UncheckedIOException, but got IOException: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        assertNull(uncheckedIOException.getCause());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCauseThrowsIllegalArgumentException() {
        try {
            new UncheckedIOException(null);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetMessage() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expectedMessage = "org.jsoup.UncheckedIOException: Test IOException";
        String actualMessage = uncheckedIOException.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}