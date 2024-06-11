package org.jsoup;

import static org.junit.Assert.instanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals("Test IOException", uncheckedIOException.getMessage());
        instanceOf(IOException.class, uncheckedIOException.getCause());
    }

    @Test
    public void testGetIoExceptionFromUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        IOException actualIoException = uncheckedIOException.ioException();
        instanceOf(IOException.class, actualIoException);
        assertEquals("Test IOException", actualIoException.getMessage());
    }

    @Test
    public void testNullUncheckedIOException() {
        assertNull(new UncheckedIOException(null));
    }

    @Test(expected = NullPointerException.class)
    public void testNullIOExceptionThrowsNPException() {
        new UncheckedIOException(null);
    }
}