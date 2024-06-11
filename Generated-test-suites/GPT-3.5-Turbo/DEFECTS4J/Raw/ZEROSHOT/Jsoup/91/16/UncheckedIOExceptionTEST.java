package org.jsoup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class UncheckedIOExceptionTest {
    private IOException ioException;
    private UncheckedIOException uncheckedIOException;

    @Before
    public void setUp() {
        ioException = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(ioException);
    }

    @After
    public void tearDown() {
        ioException = null;
        uncheckedIOException = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(uncheckedIOException);
        assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIOException() {
        assertEquals(ioException, uncheckedIOException.ioException());
    }
}