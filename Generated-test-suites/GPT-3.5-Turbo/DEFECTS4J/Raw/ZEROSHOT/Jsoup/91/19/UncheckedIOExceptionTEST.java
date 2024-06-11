package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException e = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(e);
        assertNotNull(uncheckedIOException);
        assertEquals(e, uncheckedIOException.ioException());
    }

    @Test
    public void testIOException() {
        IOException e = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(e);
        assertEquals(e, uncheckedIOException.ioException());
    }

}