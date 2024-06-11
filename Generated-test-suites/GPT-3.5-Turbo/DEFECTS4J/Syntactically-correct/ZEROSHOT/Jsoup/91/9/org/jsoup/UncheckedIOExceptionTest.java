import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    private IOException ioException;
    private UncheckedIOException uncheckedIOException;

    @Before
    public void setUp() {
        ioException = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(ioException);
    }

    @Test
    public void testIOException() {
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testCause() {
        assertEquals(ioException, uncheckedIOException.getCause());
    }
}