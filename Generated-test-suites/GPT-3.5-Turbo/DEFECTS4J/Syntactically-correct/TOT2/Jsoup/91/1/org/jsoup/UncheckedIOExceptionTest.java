package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testNullCauseInConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testCustomIOExceptionSubclass() {
        CustomIOException customIOException = new CustomIOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(customIOException);
        assertEquals(customIOException, uncheckedIOException.ioException());
    }

    class CustomIOException extends IOException {
    }

}