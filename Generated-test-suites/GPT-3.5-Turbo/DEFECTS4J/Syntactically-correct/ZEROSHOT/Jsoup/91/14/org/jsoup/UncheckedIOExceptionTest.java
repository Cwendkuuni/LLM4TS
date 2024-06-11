package org.jsoup;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIO = new UncheckedIOException(cause);

        assertEquals(cause, uncheckedIO.ioException());
    }

    @Test
    public void testUncheckedIOExceptionWithNullCause() {
        UncheckedIOException uncheckedIO = new UncheckedIOException(null);
        assertNull(uncheckedIO.ioException());
    }
}