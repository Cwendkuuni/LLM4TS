package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException exception = new UncheckedIOException(cause);
        assertEquals(cause, exception.ioException());
    }

    @Test
    public void testIoException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException exception = new UncheckedIOException(cause);
        assertEquals(cause, exception.ioException());
    }
}