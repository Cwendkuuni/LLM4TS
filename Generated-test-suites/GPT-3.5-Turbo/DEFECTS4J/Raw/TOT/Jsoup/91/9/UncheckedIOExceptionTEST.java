import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("IO Exception message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIOException() {
        IOException ioException = new IOException("IO Exception message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertEquals(ioException, uncheckedIOException.ioException());
    }
  
    @Test(expected = NullPointerException.class)
    public void testConstructorNullCause() {
        new UncheckedIOException(null);
    }

    @Test
    public void testIOExceptionNullMessage() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertEquals(ioException, uncheckedIOException.getCause());
        Assert.assertNull(ioException.getMessage());
    }

    @Test
    public void testIOExceptionEmptyMessage() {
        IOException ioException = new IOException("");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertEquals(ioException, uncheckedIOException.getCause());
        Assert.assertEquals("", ioException.getMessage());
    }

    // Additional test cases can be added here for edge cases, error scenarios, and other methods

}