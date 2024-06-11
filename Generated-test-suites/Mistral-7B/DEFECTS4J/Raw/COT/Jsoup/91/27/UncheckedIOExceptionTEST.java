import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertEquals(IOException.class, uncheckedIOException.getCause().getClass());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        IOException ioException = uncheckedIOException.ioException();
        Assert.assertEquals("Test IOException", ioException.getMessage());
    }

    @Test(expected = IOException.class)
    public void testUncheckedIOExceptionThrowsOriginalException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        Assert.assertNull(uncheckedIOException.getCause());
        Assert.assertEquals("null", uncheckedIOException.getMessage());
    }

    @Test
    public void testEmptyMessageCause() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("", uncheckedIOException.getMessage());
    }

    @Test
    public void testMultipleLayersException() {
        IOException ioException1 = new IOException("IOException 1");
        IOException ioException2 = new IOException("IOException 2");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new UncheckedIOException(ioException1), ioException2);
        IOException ioException = uncheckedIOException.ioException();
        Assert.assertEquals(ioException2.getClass(), ioException.getClass());
        Assert.assertEquals(ioException2.getMessage(), ioException.getMessage());
    }
}