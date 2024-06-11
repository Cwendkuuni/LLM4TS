package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertNotNull(uncheckedIOException.getCause());
    }

    @Test
    public void testGetCause() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertSame(cause, uncheckedIOException.getCause());
    }
}