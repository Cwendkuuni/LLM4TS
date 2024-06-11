package org.jsoup;

import org.junit.Test;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testIoException() {
        // Basic functionality test
        IOException exception = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        assert uncheckedIOException.ioException() == exception;
    }
}