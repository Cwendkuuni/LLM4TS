package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertEquals("Test IOException", uncheckedIOException.getMessage());
    }

    @Test
    public void testNullIoException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }
}