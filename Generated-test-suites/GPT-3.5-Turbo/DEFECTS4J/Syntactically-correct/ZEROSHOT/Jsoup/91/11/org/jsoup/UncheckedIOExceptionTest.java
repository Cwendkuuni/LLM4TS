import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }
}