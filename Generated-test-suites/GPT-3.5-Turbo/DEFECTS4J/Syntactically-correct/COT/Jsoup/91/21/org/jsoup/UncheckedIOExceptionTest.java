import org.jsoup.UncheckedIOException;
import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertSame(cause, uncheckedIOException.ioException());
    }
}