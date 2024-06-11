import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor_Success() {
        IOException e = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(e);
        assertEquals(e, uncheckedIOException.ioException());
    }

    @Test
    public void testConstructor_NullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIoException() {
        IOException e = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(e);
        throw uncheckedIOException.ioException();
    }

}