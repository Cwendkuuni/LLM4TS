package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testNullConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        assertEquals(null, uncheckedIOException.getCause());
    }

    @Test
    public void testNullIoException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        assertEquals(null, uncheckedIOException.ioException());
    }

    @Test
    public void testCastingException() {
        RuntimeException runtimeException = new RuntimeException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(runtimeException);

        assertEquals(runtimeException, uncheckedIOException.getCause());
        assertTrue(uncheckedIOException.ioException() instanceof IOException);
    }
}