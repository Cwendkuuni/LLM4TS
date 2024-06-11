package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException exception = new IOException();
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);

        assertNotNull(uncheckedException);
        assertEquals(exception, uncheckedException.getCause());
    }

    @Test
    public void testIoException() {
        IOException exception = new IOException();
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);

        assertEquals(exception, uncheckedException.ioException());
    }
}