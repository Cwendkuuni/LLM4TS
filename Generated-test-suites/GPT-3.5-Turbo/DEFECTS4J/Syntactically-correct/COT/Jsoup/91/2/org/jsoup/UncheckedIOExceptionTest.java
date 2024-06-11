package org.jsoup;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
}