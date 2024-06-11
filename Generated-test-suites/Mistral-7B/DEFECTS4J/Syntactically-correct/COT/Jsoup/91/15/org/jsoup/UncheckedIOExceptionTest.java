import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionMethodNormalScenario() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testIoExceptionMethodThrowsException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        uncheckedIOException.ioException();
    }

    @Test
    public void testNullConstructorScenario() {
        try {
            new UncheckedIOException(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testNullMessageScenario() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null, cause);
        Assert.assertNull(uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testEmptyMessageScenario() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException("", cause);
        Assert.assertEquals("", uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testNullCauseScenario() {
        try {
            new UncheckedIOException(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}