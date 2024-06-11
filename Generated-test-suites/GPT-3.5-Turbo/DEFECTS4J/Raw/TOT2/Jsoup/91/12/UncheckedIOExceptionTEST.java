package org.jsoup;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    // Expert 1 test cases
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertNotNull(uncheckedIOException);
    }

    @Test
    public void testIOException() {
        IOException ioException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testConstructorWithNullInput() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNotNull(uncheckedIOException);
    }

    // Expert 2 test case
    @Test
    public void testIOExceptionDifferentType() {
        RuntimeException exception = new RuntimeException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException((IOException) exception);
        assertNull(uncheckedIOException.ioException());
    }

    // Expert 3 test cases
    @Test
    public void testConstructorWithIOException() {
        IOException ioException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIOExceptionNotNull() {
        IOException ioException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertNotNull(uncheckedIOException.ioException());
    }
}

