package org.jsoup;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException ioException = new IOException("test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException ioException = new IOException("test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testUncheckedIOExceptionConstructorNull() {
        new UncheckedIOException(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIoExceptionMethodNull() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        uncheckedIOException.ioException();
    }

    @Test
    public void testUncheckedIOExceptionConstructorSubclass() {
        FileNotFoundException fileNotFoundException = new FileNotFoundException("test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(fileNotFoundException);

        assertEquals(fileNotFoundException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionMethodSubclass() {
        FileNotFoundException fileNotFoundException = new FileNotFoundException("test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(fileNotFoundException);

        assertEquals(fileNotFoundException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionConstructorNoCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        assertNull(uncheckedIOException.getCause());
    }
}