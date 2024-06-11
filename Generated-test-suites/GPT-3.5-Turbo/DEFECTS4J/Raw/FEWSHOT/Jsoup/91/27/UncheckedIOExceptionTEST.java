package org.jsoup;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException_constructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertTrue(ioException == uncheckedIOException.ioException());
        assertTrue(ioException.getMessage().equals(uncheckedIOException.getMessage()));
    }
}