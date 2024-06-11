package org.jsoup;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test
    public void testIOException() {
        IOException ioException = new IOException("Test");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test
    public void testRuntimeException() {
        IOException ioException = new IOException("Test");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertTrue(uncheckedIOException instanceof RuntimeException);
    }
  
}