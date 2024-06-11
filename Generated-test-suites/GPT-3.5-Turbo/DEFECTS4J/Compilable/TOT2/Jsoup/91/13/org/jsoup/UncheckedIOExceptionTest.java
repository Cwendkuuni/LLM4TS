package org.jsoup;
import org.junit.Test;
import org.jsoup.UncheckedIOException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIoException = new UncheckedIOException(ioException);
        assertNotNull(uncheckedIoException);
        assertEquals(ioException, uncheckedIoException.ioException());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIoException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIoException.ioException());
    }
}