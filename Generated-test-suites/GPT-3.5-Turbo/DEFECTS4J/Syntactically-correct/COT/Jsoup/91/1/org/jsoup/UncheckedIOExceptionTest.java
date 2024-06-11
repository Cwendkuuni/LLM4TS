import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testIOExceptionConstructor() {
        IOException ioException = new IOException("IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
        assertEquals(ioException.getMessage(), uncheckedIOException.getMessage());
        
        assertEquals(null, uncheckedIOException.getCause());
    }
}