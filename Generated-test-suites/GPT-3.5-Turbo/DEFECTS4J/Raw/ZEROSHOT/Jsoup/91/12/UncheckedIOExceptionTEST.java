package org.jsoup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    private UncheckedIOException uncheckedIOException;

    @Before
    public void setUp() {
        IOException ioException = new IOException("I/O Exception");
        uncheckedIOException = new UncheckedIOException(ioException);
    }

    @After
    public void tearDown() {
        uncheckedIOException = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(uncheckedIOException);
    }

    @Test
    public void testIoException() {
        IOException expected = new IOException("I/O Exception");
        IOException actual = uncheckedIOException.ioException();
        assertEquals(expected.getMessage(), actual.getMessage());
    }
}