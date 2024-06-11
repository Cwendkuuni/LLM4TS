package org.jsoup;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UncheckedIOExceptionTest {

    private UncheckedIOException uncheckedIOException;
    private IOException ioException;

    @Before
    public void setUp() {
        ioException = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(ioException);
    }

    @Test
    public void testUncheckedIOExceptionWithCause() {
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionWithNullCause() {
        UncheckedIOException exception = new UncheckedIOException(null);
        assertNull(exception.ioException());
    }
}