package org.jsoup;
//Import necessary dependencies for testing
import java.io.IOException;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructorWithCause() {
        IOException cause = new IOException("IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertEquals(cause, uncheckedIOException.getCause());
    }

    @Test
    public void testIoException() {
        IOException cause = new IOException("IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertEquals(cause, uncheckedIOException.ioException());
    }
}