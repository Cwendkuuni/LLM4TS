package org.jsoup;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("I/O Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test
    public void testIOException() {
        IOException ioException = new IOException("I/O Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}