package org.jsoup.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("Test IOException", uncheckedIOException.getMessage());
        assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testUncheckedIOExceptionThrown() throws IOException {
        IOException ioException = new IOException("Test IOException");
        throw new UncheckedIOException(ioException);
    }

    @Test
    public void testUncheckedIOExceptionIsUnchecked() {
        try {
            throw new UncheckedIOException(new IOException("Test IOException"));
        } catch (IOException e) {
            fail("An IOException was caught instead of an UncheckedIOException.");
        }
    }

    @Test
    public void testUncheckedIOExceptionIsInstanceOfRuntimeException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException"));
        assertTrue(uncheckedIOException instanceof RuntimeException);
    }

    @Test
    public void testUncheckedIOExceptionIsInstanceOfIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException"));
        assertTrue(uncheckedIOException instanceof IOException);
    }
}