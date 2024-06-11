package org.example.junit;

import org.jsoup.UncheckedIOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceof(RuntimeException.class, uncheckedIOException);
        assertInstanceof(IOException.class, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        IOException actualIoException = uncheckedIOException.ioException();
        assertSame(ioException, actualIoException);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    }

    @Test
    public void testIoExceptionMethodWithNullUncheckedIOException() {
        UncheckedIOException uncheckedIOException = null;
        IOException actualIoException = uncheckedIOException.ioException();
        assertNull(actualIoException);
    }

    @Test
    public void testIoExceptionMethodWithProperUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        IOException actualIoException = uncheckedIOException.ioException();
        assertSame(ioException, actualIoException);
    }

}