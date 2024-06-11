import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertNotNull(uncheckedIOException);
        assertEquals(ioException, uncheckedIOException.getCause());
    }
    
    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    // Edge Cases and Exception Handling
    @Test
    public void testConstructorNull() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        
        assertNull(uncheckedIOException.getCause());
    }
    
    @Test(expected = ClassCastException.class)
    public void testIoExceptionClassCastException() {
        RuntimeException runtimeException = new RuntimeException("Test RuntimeException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(runtimeException);
        
        uncheckedIOException.ioException();
    }
}