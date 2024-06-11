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
    public void testThrowUncheckedIOException() {
        try {
            throw new UncheckedIOException(new IOException("Test IOException"));
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionThrownToUncheckedIOException() {
        try {
            throw new IOException("Test IOException");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    public void testEquals() {
        IOException ioException1 = new IOException("Test IOException 1");
        IOException ioException2 = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(ioException1);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(ioException2);
        Assert.assertNotEqual(uncheckedIOException1, uncheckedIOException2);
        UncheckedIOException uncheckedIOException3 = new UncheckedIOException(ioException1);
        Assert.assertEquals(uncheckedIOException1, uncheckedIOException3);
    }

    @Test
    public void testHashCode() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(ioException);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(ioException);
        Assert.assertEquals(uncheckedIOException1.hashCode(), uncheckedIOException2.hashCode());
    }

    @Test
    public void testToString() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        String expected = "UncheckedIOException: Test IOException";
        String actual = uncheckedIOException.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenNullCause() {
        try {
            new UncheckedIOException(null);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCauseReturnsNullWhenNoCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertNull(uncheckedIOException.getCause());
    }

    @Test
    public void testGetCauseReturnsIOExceptionWhenIOExceptionPassed() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }
}