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
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testThrowIOExceptionOnGetCause() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.getCause();
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        Assert.assertNull(uncheckedIOException.getCause());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    }

    @Test
    public void testToString() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("org.jsoup.UncheckedIOException: Test IOException", uncheckedIOException.toString());
    }

    @Test
    public void testEquals() {
        IOException cause1 = new IOException("Test IOException 1");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(cause1);
        IOException cause2 = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(cause2);
        Assert.assertNotSame(uncheckedIOException1, uncheckedIOException2);
        UncheckedIOException uncheckedIOException3 = new UncheckedIOException(cause1);
        Assert.assertEquals(uncheckedIOException1, uncheckedIOException3);
    }

    @Test
    public void testHashCode() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(cause);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(cause);
        Assert.assertEquals(uncheckedIOException1.hashCode(), uncheckedIOException2.hashCode());
    }
}