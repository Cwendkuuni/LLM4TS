import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOExceptionConstructor() {
        IOException ioException = new IOException("IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIOExceptionConstructorWithNull() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        Assert.assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testIOExceptionConstructorWithCustomException() {
        RuntimeException runtimeException = new RuntimeException("Custom exception message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(runtimeException);

        Assert.assertEquals(runtimeException, uncheckedIOException.getCause());
        Assert.assertNull(uncheckedIOException.ioException());
    }

    @Test(expected = ClassCastException.class)
    public void testIOExceptionConstructorWithCustomExceptionAndNotIOException() {
        Exception exception = new Exception("Custom exception message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        Assert.assertEquals(exception, uncheckedIOException.getCause());
        Assert.assertNull(uncheckedIOException.ioException());
    }
}