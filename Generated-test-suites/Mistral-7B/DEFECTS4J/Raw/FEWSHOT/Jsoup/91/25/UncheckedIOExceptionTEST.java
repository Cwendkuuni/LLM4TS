package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertEquals("Test IOException", uncheckedIOException.getMessage());
    }

    @Test
    public void testEmptyUncheckedIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException((IOException) null);
        assertNull(uncheckedIOException.ioException());
        assertEquals("", uncheckedIOException.getMessage());
    }
}