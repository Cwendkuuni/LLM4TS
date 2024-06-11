package org.jsoup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testConstructor() {
        IOException exception = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        assertNotNull(uncheckedIOException);
        assertEquals(exception, uncheckedIOException.ioException());
    }
    
    @Test
    public void testIOException() {
        IOException exception = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedIOException.ioException());
    }

}