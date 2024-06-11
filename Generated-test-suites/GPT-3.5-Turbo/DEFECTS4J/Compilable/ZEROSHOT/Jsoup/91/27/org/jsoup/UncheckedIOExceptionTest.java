package org.jsoup;


import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
        assertEquals("Test Exception", uncheckedIOException.ioException().getMessage());
    }

    @Test
    public void testIOException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }

}