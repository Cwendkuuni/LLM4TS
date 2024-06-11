package org.jsoup;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionCreation() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIOExceptionThrown() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        try {
            throw uncheckedIOException;
        } catch (UncheckedIOException e) {
            assertEquals(ioException, e.ioException());
        }
    }
    
}