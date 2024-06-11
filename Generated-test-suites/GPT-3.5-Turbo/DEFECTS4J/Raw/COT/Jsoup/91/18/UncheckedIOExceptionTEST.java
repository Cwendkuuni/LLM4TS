import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOException() {
        IOException e = new IOException();
        UncheckedIOException uncheckedEx = new UncheckedIOException(e);
        
        assertEquals(e, uncheckedEx.ioException());
    }
    
    // Edge cases and exception handling scenarios
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNull() {
        new UncheckedIOException(null);
    }
    
    @Test(expected = ClassCastException.class)
    public void testConstructorInvalidType() {
        new UncheckedIOException(new RuntimeException());
    }
    
    @Test
    public void testConstructor() {
        IOException e = new IOException();
        UncheckedIOException uncheckedEx = new UncheckedIOException(e);
        
        assertNotNull(uncheckedEx);
        assertEquals(e, uncheckedEx.ioException());
    }
    
}