
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("Test IOException", uncheckedIOException.getMessage());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testNullIoException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    }

    @Test
    public void testEmptyMessageIoException() {
        IOException ioException = new IOException("");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("", uncheckedIOException.getMessage());
    }

    @Test
    public void testEmptyMessageUncheckedIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException(""));
        assertEquals("", uncheckedIOException.getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testNullUncheckedIOException() {
        UncheckedIOException uncheckedIOException = null;
    }
}