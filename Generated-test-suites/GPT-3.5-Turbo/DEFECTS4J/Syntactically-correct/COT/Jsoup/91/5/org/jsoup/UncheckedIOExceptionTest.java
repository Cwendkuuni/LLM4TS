import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testIoException() {
        // Test Object
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        // Assertion
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testUncheckedIOException() throws IOException {
        // Test Object
        IOException ioException = new IOException("Test Exception");

        // Should throw UncheckedIOException
        throw new UncheckedIOException(ioException);
    }
}