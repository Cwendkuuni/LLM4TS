import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}