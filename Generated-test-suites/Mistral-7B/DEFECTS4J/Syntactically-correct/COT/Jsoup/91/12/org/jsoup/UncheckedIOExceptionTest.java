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
    public void testIOExceptionRetrieval() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testUncheckedIOExceptionThrowsWrappedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testNullConstructor() {
        try {
            UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNullMessageInConstructor() {
        IOException ioException = new IOException("Test IOException");
        try {
            UncheckedIOException uncheckedIOException = new UncheckedIOException(null, ioException);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNullCauseInConstructor() {
        try {
            UncheckedIOException uncheckedIOException = new UncheckedIOException("Test Message", null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNullMessageAndNullCauseInConstructor() {
        try {
            UncheckedIOException uncheckedIOException = new UncheckedIOException(null, null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }
}