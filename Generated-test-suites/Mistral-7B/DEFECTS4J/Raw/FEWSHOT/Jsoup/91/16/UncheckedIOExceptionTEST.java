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
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
    }

    @Test
    public void testNullArgument() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
        assertInstanceOf(NullPointerException.class, uncheckedIOException.getCause());
    }

    @Test
    public void testGetCause() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertSame(cause, uncheckedIOException.getCause());
    }
}