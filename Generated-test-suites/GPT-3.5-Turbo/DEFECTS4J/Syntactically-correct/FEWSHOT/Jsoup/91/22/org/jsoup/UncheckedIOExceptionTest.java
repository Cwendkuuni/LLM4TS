package org.jsoup;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIO = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIO.ioException());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIO = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIO.ioException());
    }
}