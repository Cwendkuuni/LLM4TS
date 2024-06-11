package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Test cause");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertEquals("Test cause", uncheckedIOException.getMessage());
        assertEquals(cause.getClass(), uncheckedIOException.getClass());
    }

    @Test
    public void testNullIoException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
        assertNull(uncheckedIOException.getCause());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionThrown() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test cause"));
        uncheckedIOException.ioException().printStackTrace(); // Printing to facilitate testing
        // The test case should throw an IOException here
    }
}