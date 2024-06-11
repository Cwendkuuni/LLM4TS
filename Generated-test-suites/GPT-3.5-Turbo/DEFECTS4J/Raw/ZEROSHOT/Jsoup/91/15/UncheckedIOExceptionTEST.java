package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {

    @Test
    public void testIoException() throws IOException {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        
        assertEquals(exception, uncheckedIOException.ioException());
    }
    
    @Test(expected = NullPointerException.class)
    public void testCauseNullException() {
        new UncheckedIOException(null);
    }
    
    @Test
    public void testToString() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        
        assertEquals("org.jsoup.UncheckedIOException: java.io.IOException: Test Exception", uncheckedIOException.toString());
    }
    
}