package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Test Exception");
        UncheckedIOException exception = new UncheckedIOException(cause);
        assertEquals(cause, exception.ioException());
    }
}