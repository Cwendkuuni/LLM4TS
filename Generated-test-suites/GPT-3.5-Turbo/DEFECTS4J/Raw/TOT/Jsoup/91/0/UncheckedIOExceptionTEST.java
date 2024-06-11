import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test
    public void testUninitializedUncheckedIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        
        assertNull(uncheckedIOException.ioException());
    }
    
    @Test
    public void testNestedException() {
        IOException nestedException = new IOException("Nested IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(nestedException);
        
        assertEquals(nestedException, uncheckedIOException.ioException());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        new UncheckedIOException(null);
    }
}