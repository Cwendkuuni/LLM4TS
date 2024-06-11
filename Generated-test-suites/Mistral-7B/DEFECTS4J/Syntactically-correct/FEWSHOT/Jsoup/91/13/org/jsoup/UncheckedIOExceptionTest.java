package org.jsoup;

import static org.junit.Assert.assertInstanceOf;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testThrowIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        uncheckedIOException.throwIOException();
    }
}