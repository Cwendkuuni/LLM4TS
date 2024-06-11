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
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
    }

    @Test
    public void testGetCauseReturnsOriginalIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIOException.getCause());
    }

    @Test
    public void testNullException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
    }
}