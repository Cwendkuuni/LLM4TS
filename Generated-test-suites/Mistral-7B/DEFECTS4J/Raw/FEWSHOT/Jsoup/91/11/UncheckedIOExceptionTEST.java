package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertTrue(uncheckedIOException.getMessage().equals("Test IOException"));
    }

    @Test(expected = IOException.class)
    public void testUncheckedIOExceptionThrowsIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException"));
        uncheckedIOException.ioException();
    }
}