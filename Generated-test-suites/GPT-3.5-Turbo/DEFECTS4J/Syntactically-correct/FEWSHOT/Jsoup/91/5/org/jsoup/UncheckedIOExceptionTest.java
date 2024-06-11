package org.jsoup;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}