package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test IO exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertNotNull(uncheckedIOException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testNullException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        
        assertNotNull(uncheckedIOException);
        assertEquals(null, uncheckedIOException.ioException());
    }

    @Test
    public void testIOException() {
        IOException ioException = new IOException("Test IO exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertNotNull(uncheckedIOException.ioException());
        assertEquals(ioException, uncheckedIOException.ioException());
        assertTrue(uncheckedIOException.ioException().getMessage().equals("Test IO exception"));
    }

}