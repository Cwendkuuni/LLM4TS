import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;


public class UncheckedIOExceptionTest {

    private UncheckedIOException uncheckedIOException;

    @Before
    public void setup() {
        IOException ioException = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(ioException);
    }

    @Test
    public void testIOException() {
        IOException ioException = uncheckedIOException.ioException();
        assertNotNull(ioException);
        assertEquals("Test IOException", ioException.getMessage());
    }

}