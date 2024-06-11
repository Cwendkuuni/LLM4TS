package org.jsoup;
import org.jsoup.UncheckedIOException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException exception = new IOException("test");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        assertEquals("test", uncheckedIOException.getCause().getMessage());
    }

    @Test
    public void testIoException() {
        IOException exception = new IOException("test");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        assertNotNull(uncheckedIOException.ioException());
        assertEquals("test", uncheckedIOException.ioException().getMessage());
    }

    @Test
    public void testNullConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        assertEquals(null, uncheckedIOException.getCause());
    }

}