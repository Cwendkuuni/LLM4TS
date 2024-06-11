package org.jsoup;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOException() {
        IOException expected = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(expected);
        
        assertEquals(expected, uncheckedIOException.ioException());
    }
}