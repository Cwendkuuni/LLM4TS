import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testUncheckedIOExceptionThrowsCorrectException() {
        IOException ioException = new IOException("Test IOException");
        throw new UncheckedIOException(ioException);
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertNull(uncheckedIOException.ioException());
        Assert.assertTrue(uncheckedIOException.getMessage().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new UncheckedIOException(null);
    }

    @Test
    public void testNullMessageInUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException, null);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testEmptyMessageAndNullIoException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertTrue(uncheckedIOException.getMessage().isEmpty());
        Assert.assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionWithCustomMessage() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException("Custom message", ioException);
        Assert.assertEquals("Custom message", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }
}