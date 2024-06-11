package org.jsoup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    private UncheckedIOException uncheckedIOException;

    @Before
    public void setUp() throws IOException {
        IOException ioException = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(ioException);
    }

    @After
    public void tearDown() {
        uncheckedIOException = null;
    }

    @Test
    public void testIOException() {
        IOException ioException = uncheckedIOException.ioException();
        assertNotNull(ioException);
        assertEquals("Test IOException", ioException.getMessage());
    }

    @Test
    public void testCause() {
        IOException ioException = uncheckedIOException.ioException();
        assertNotNull(ioException);
        assertEquals("Test IOException", ioException.getMessage());
    }
}