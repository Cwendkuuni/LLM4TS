import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testNullIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertEquals(null, uncheckedIOException.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testNullExceptionInIoException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        uncheckedIOException.ioException().getMessage();
    }
}