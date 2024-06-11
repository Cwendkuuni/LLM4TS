import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException exception = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        assertSame(exception, uncheckedIOException.ioException());
    }
}