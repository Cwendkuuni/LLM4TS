import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertNotNull(uncheckedIOException);
        assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new UncheckedIOException(null);
    }
}