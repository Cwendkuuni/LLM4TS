import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOExceptionConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}