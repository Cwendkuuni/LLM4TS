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
    public void testIoExceptionMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testThrowOriginalIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.ioException();
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testNullConstructor() {
        Assert.assertNull(new UncheckedIOException(null));
    }

    @Test(expected = NullPointerException.class)
    public void testNullCauseConstructor() {
        new UncheckedIOException(null);
    }

    @Test
    public void testToStringMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expected = "org.jsoup.UncheckedIOException: Test IOException";
        String actual = uncheckedIOException.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expected = "Test IOException";
        String actual = uncheckedIOException.getMessage();
        Assert.assertEquals(expected, actual);
    }
}