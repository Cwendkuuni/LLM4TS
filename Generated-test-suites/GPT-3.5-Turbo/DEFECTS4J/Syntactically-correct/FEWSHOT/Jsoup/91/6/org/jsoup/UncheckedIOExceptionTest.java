package org.jsoup;

import org.junit.Test;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionConstructorWithNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionConstructorWithMessage() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test
    public void testIoException() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}