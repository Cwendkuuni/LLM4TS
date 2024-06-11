import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException io = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(io);
        assertEquals(io, uncheckedIOException.ioException());
    }
}