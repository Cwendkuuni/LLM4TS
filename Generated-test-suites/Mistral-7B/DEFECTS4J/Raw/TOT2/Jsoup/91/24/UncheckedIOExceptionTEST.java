import org.junit.Test;
import static org.junit.Assert.assertInstanceOf;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }
}