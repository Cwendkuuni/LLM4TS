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
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    @Test
    public void testIoException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        Assert.assertEquals("Test IOException", uncheckedIOException.ioException().getMessage());
    }

    @Test(expected = IOException.class)
    public void testIoExceptionThrowsIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException"));
        try {
            uncheckedIOException.ioException();
        } catch (IOException e) {
        }
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertNull(uncheckedIOException.getCause());
        Assert.assertNull(uncheckedIOException.getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testNullCauseConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    }

    @Test
    public void testEquals() {
        IOException cause1 = new IOException("Test IOException 1");
        IOException cause2 = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(cause1);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(cause2);
        Assert.assertNotEquals(uncheckedIOException1, uncheckedIOException2);
        UncheckedIOException uncheckedIOException3 = new UncheckedIOException(cause1);
        Assert.assertEquals(uncheckedIOException1, uncheckedIOException3);
    }
}