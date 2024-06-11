
package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testIOExceptionConstructor() {
        IOException testException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(testException);
        
        assertNotNull(uncheckedIOException);
        assertEquals(testException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException testException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(testException);
        
        assertEquals(testException, uncheckedIOException.ioException());
    }

}
