import org.junit.Test;
import static org.junit.Assert.assertInstanceOf;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }
}