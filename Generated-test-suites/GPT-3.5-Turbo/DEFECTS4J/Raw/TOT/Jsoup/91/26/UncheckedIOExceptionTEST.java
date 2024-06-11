package org.jsoup;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        UncheckedIOException exception = new UncheckedIOException(new IOException("Test IO Exception"));
        assertNotNull(exception);
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception.getCause() instanceof IOException);
        assertEquals("Test IO Exception", exception.getCause().getMessage());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException exception = new UncheckedIOException(ioException);
        assertSame(ioException, exception.ioException());
    }

    @Test
    public void testNullCause() {
        UncheckedIOException exception = new UncheckedIOException(null);
        assertNull(exception.ioException());
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidCause() {
        UncheckedIOException exception = new UncheckedIOException(new RuntimeException("Test exception"));
        exception.ioException();
    }
}