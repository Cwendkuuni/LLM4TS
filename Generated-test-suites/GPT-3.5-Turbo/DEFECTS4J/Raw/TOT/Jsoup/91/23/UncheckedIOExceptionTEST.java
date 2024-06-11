import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(ioException);

        assertNotNull(uncheckedException);
        assertEquals(ioException, uncheckedException.getCause());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedException.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNull() {
        new UncheckedIOException(null);
    }

}