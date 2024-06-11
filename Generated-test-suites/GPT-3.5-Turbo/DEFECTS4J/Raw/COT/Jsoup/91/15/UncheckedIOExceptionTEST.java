import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testIoException() {
        // Test when a cause is passed to the constructor
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedException.ioException());
        
        // Test when no cause is passed to the constructor
        UncheckedIOException uncheckedException2 = new UncheckedIOException(null);
        assertNull(uncheckedException2.ioException());
    }
}