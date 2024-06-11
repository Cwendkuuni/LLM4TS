package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        assertInstanceOf(IOException.class, uncheckedException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedException);
    }

    @Test
    public void testNullIoException() {
        UncheckedIOException uncheckedException = new UncheckedIOException(null);
        assertNull(uncheckedException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedException);
    }
}