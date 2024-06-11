import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException exception = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assertSame(exception, uncheckedException.ioException());
    }

    @Test
    public void testIOException() {
        IOException exception = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assertSame(exception, uncheckedException.ioException());
    }

}