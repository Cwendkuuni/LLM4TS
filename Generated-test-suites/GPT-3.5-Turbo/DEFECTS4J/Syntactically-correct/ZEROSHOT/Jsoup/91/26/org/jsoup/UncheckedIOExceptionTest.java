package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException exception = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        assertEquals("Test IOException", uncheckedIOException.getMessage());
        assertSame(exception, uncheckedIOException.ioException());
    }

    @Test
    public void testIOException() {
        IOException exception = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        assertSame(exception, uncheckedIOException.ioException());
    }
}