package org.jsoup;

import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;


public class UncheckedIOExceptionTest {

    @Test
    public void testCreation() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assert uncheckedIOException.getCause() == ioException;
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assert uncheckedIOException.ioException() == ioException;
    }

}