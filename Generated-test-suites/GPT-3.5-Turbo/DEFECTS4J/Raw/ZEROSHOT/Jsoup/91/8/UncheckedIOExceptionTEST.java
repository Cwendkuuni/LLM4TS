package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOExceptionShouldBeSet() {
        IOException expectedCause = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(expectedCause);

        assertEquals(expectedCause, uncheckedIOException.ioException());
    }

}