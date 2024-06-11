
package org.jsoup;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testIOExceptionConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test
    public void testGetCause() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.getCause());
    }
}