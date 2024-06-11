package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIOException() {
        IOException ioException = new IOException("test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}