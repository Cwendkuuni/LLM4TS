import org.junit.Test;
import org.junit.Assert;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        Assert.assertNull(uncheckedIOException.getCause());
    }

}