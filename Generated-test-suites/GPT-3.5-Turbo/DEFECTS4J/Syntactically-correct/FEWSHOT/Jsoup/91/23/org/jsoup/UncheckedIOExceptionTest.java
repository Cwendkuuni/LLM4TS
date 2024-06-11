package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UncheckedIOExceptionTest {
    private static final IOException testException = new IOException("Test exception");
    private static final UncheckedIOException uncheckedIOException = new UncheckedIOException(testException);

    @Test
    public void testConstructor() {
        assertEquals(testException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoException() {
        assertEquals(testException, uncheckedIOException.ioException());
    }
}