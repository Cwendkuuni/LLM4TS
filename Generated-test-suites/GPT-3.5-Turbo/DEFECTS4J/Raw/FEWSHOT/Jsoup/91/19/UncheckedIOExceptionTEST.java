package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedException.ioException());
    }
    
    @Test
    public void testIoException() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedException.ioException());
    }
    
    @Test
    public void testIOExceptionWithNullCause() {
        UncheckedIOException uncheckedException = new UncheckedIOException(null);
        assertNull(uncheckedException.ioException());
    }
}