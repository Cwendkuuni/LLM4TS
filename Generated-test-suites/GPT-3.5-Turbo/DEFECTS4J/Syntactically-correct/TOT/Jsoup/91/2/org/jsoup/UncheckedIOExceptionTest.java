package org.jsoup;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOExceptionConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCause() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIOExceptionConstructorWithNull() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        assertNull(uncheckedIOException.ioException());
    }

    @Test(expected = FileNotFoundException.class)
    public void testIOExceptionConstructorWithFileNotFoundException() throws IOException {
        throw new FileNotFoundException("Test FileNotFoundException");
    }

    @Test(expected = IOException.class)
    public void testIOExceptionConstructorWithIOException() throws IOException {
        throw new IOException("Test IOException");
    }

    @Test(expected = RuntimeException.class)
    public void testIOExceptionConstructorWithRuntimeException() throws IOException {
        throw new RuntimeException("Test RuntimeException");
    }

}