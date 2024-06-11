import org.junit.Test;
import java.io.IOException;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}