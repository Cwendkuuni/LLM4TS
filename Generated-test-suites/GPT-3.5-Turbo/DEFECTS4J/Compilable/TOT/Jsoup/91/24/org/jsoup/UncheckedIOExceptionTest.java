package org.jsoup;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructorAndGetCause() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.getCause());
    }
    
    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
}