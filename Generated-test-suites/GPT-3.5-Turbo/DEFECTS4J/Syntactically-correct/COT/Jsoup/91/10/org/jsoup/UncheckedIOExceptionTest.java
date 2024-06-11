import static org.junit.Assert.*;
import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testIoException() {
        IOException expectedCause = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(expectedCause);

        assertEquals(expectedCause, uncheckedIOException.ioException());
    }

}