import org.junit.Test;
import org.junit.Assert.*;
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
    public void testMethodThatThrowsIOExceptionAndIsWrapped() {
        // Given a method that throws an IOException
        MyClass myClass = new MyClass();

        try {
            myClass.someMethod();
        } catch (UncheckedIOException e) {
            // When the IOException is wrapped in an UncheckedIOException
            IOException ioException = e.ioException();

            // Then the original IOException message is available
            String expectedMessage = "Expected IOException Message";
            Assert.assertEquals(expectedMessage, ioException.getMessage());
            // And the test fails, as expected, since IOException is being thrown
            throw ioException;
        }
    }

    @Test
    public void testUncheckedIOExceptionWithNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        Assert.assertNull(uncheckedIOException.getCause());
        Assert.assertNull(uncheckedIOException.ioException());
        Assert.assertEquals("null", uncheckedIOException.getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullCauseAndThrowNPE() {
        new UncheckedIOException(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNonIOExceptionCauseAndThrowIAE() {
        new UncheckedIOException(new RuntimeException("Test RuntimeException"));
    }
}