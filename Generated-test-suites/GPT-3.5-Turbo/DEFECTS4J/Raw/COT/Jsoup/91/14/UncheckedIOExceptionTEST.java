import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test
    public void testUncheckedIOException_NullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }
    
    @Test(expected = NullPointerException.class)
    public void testUncheckedIOException_WithNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        uncheckedIOException.ioException().getMessage();
    }
}