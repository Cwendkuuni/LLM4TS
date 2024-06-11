import org.jsoup.UncheckedIOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioe = new IOException("Test IOException");
        UncheckedIOException uie = new UncheckedIOException(ioe);
        assertSame(ioe, uie.ioException());
    }
}