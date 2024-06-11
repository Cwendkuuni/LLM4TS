package org.jsoup;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    
    private UncheckedIOException uncheckedIOException;
    private IOException ioException;
    
    @Before
    public void setup() {
        ioException = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(ioException);
    }
    
    @Test
    public void testUncheckedIOExceptionConstructor() {
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}
