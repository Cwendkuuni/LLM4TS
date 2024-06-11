package org.jsoup;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIO = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIO.ioException());
        assertEquals(ioException.getMessage(), uncheckedIO.getMessage());
    }
}
