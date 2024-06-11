package org.jsoup;
import org.jsoup.UncheckedIOException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructorAndGetCause() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertNotNull(uncheckedIOException);
        assertEquals("Test IOException", uncheckedIOException.getCause().getMessage());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertNotNull(uncheckedIOException);
        assertEquals("Test IOException", uncheckedIOException.ioException().getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testNullIoException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        assertNotNull(uncheckedIOException);
        uncheckedIOException.ioException();
    }
}