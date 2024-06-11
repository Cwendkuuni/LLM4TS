package org.jsoup;

import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Sample IO exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Sample IO exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionNull() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        assertNull(uncheckedIOException.ioException());
    }

    @Test(expected = ClassCastException.class)
    public void testIoExceptionClassCastException() {
        RuntimeException runtimeException = new RuntimeException("Sample Runtime exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(runtimeException);

        uncheckedIOException.ioException();
    }

}