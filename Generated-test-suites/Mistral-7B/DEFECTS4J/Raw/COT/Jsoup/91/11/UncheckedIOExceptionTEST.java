import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testIoException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test
    public void testNormalOperation() {
        try {
            // Method under test should not throw an exception here
        } catch (UncheckedIOException e) {
            Assert.fail("Unexpected UncheckedIOException");
        }
    }

    @Test(expected = UncheckedIOException.class)
    public void testIoExceptionThrown() throws IOException {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        throw uncheckedIOException;
    }

    @Test(expected = NullPointerException.class)
    public void testNullIOExceptionThrown() {
        try {
            new UncheckedIOException(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testEmptyMessage() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("", uncheckedIOException.getMessage());
    }

    @Test
    public void testCustomMessage() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
    }
}