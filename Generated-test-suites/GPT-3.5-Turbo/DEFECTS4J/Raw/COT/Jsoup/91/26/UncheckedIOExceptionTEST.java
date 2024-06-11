
import org.junit.*;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    
    private UncheckedIOException uncheckedIOException;
    private IOException ioException;
    
    @Before
    public void setUp() {
        ioException = new IOException("Test exception");
        uncheckedIOException = new UncheckedIOException(ioException);
    }
    
    @Test
    public void testIOException() {
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}
