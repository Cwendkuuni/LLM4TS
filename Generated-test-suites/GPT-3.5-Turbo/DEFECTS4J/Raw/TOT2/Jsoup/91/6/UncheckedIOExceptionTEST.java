import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void constructorTest() {
        IOException testException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(testException);

        assertEquals("Constructor should set the cause correctly", testException, uncheckedIOException.getCause());
    }

    @Test
    public void ioExceptionTest() {
        IOException testException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(testException);

        assertEquals("IOException method should return the original exception", testException, uncheckedIOException.ioException());
    }
}