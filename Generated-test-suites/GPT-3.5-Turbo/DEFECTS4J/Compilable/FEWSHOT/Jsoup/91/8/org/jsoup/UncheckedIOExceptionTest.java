package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedIOException.ioException());
    }

    @Test
    public void testConstructorWithNull() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }

}