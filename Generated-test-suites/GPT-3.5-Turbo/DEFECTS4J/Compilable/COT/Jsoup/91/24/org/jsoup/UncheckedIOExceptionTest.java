package org.jsoup;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOException() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        
        assertEquals(exception, uncheckedIOException.ioException());
    }
}
