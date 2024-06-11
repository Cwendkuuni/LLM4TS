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
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        Assert.assertEquals("Test IOException", uncheckedIOException.ioException().getMessage());
    }

    @Test(expected = IOException.class)
    public void testThrowOriginalException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.ioException();
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertNull(uncheckedIOException.getCause());
    }

    @Test
    public void testNullCauseConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        Assert.assertNull(uncheckedIOException.getCause());
    }

    @Test(expected = NullPointerException.class)
    public void testNullMessageThreeArgConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException"), "Null Message");
    }

    @Test
    public void testThreeArgConstructorMessage() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, "Test Message");
        Assert.assertEquals("Test Message", uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    @Test(expected = IOException.class)
    public void testThreeArgConstructorThrowOriginalException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, "Test Message");
        uncheckedIOException.ioException();
    }
}