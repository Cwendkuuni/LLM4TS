import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}