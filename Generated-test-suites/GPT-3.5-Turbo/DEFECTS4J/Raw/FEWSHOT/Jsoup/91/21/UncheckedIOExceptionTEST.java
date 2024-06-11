package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertNotNull(uncheckedIOException);
        assertEquals(ioException, uncheckedIOException.getCause());
    }
    
    @Test
    public void testIoException() {
        IOException ioException = new IOException("IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }
}