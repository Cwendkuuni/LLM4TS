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
        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertNull(uncheckedIOException.ioException());
        Assert.assertNull(uncheckedIOException.getMessage());
    }

    @Test
    public void testNullConstructor() {
        try {
            new UncheckedIOException(null);
            Assert.fail("Expected IllegaleArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullMessage() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, null);
        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testEmptyMessageAndNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testEmptyMessageAndValidCause() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, "");
        Assert.assertEquals(cause.getMessage(), uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.ioException());
        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testValidMessageAndNullCause() {
        String message = "Test Message";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(message, null);
        Assert.assertEquals(message, uncheckedIOException.getMessage());
        Assert.assertNull(uncheckedIOException.ioException());
        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testValidMessageAndValidCause() {
        String message = "Test Message";
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(message, cause);
        Assert.assertEquals(message, uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.ioException());
        uncheckedIOException.printStackTrace();
    }
}