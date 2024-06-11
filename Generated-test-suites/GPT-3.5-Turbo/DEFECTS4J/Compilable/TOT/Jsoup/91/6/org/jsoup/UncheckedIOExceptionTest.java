package org.jsoup;
import org.jsoup.UncheckedIOException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        // Typical use case
        IOException exception = new IOException("IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedIOException.getCause());

        // Error scenario: passing a null IOException should throw NullPointerException
        try {
            new UncheckedIOException(null);
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testIoException() {
        // Typical use case
        IOException exception = new IOException("IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        assertEquals(exception, uncheckedIOException.ioException());

        // Edge case: calling ioException() without setting the cause should return null
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(null);
        assertEquals(null, uncheckedIOException2.ioException());
    }
}