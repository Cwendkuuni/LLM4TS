
import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testIoException() {
        IOException exception = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        assertEquals(exception, uncheckedIOException.ioException());
    }
}
