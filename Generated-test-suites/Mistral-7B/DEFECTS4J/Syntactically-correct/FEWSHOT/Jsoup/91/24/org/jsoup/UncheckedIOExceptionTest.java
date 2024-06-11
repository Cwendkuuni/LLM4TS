package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertEquals("Expected IOException", uncheckedIOException.getMessage());
    }

    @Test(expected = IOException.class)
    public void testThrowOriginalIOException() {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.ioException();
    }
}