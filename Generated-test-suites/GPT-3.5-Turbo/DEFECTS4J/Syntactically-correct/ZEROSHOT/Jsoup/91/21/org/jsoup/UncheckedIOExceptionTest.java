import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedIOException.getCause());
    }

    @Test
    public void testIoException() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedIOException.ioException());
    }

}