package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructorWithIOException() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertNotNull(uncheckedIOException.ioException());
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}