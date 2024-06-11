package org.jsoup;

import org.junit.Test;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedException.getCause());
    }

    @Test
    public void testIOException() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assertSame(exception, uncheckedException.ioException());
    }

    @Test
    public void testIOException_NullCause() {
        UncheckedIOException uncheckedException = new UncheckedIOException(null);
        assertNull(uncheckedException.ioException());
    }

}