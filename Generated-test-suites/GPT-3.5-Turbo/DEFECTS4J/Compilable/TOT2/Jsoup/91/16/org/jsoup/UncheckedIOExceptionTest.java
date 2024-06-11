package org.jsoup;
import org.jsoup.UncheckedIOException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertNotNull(uncheckedIOException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCause() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        assertNotNull(uncheckedIOException);
    }

}