
package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("IO exception message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("Constructor should set the correct cause", ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("IO exception message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("ioException() method should return the correct IOException", ioException, uncheckedIOException.ioException());
    }
}
