package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIoException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIoException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedIoException);
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIoException = new UncheckedIOException();
        assertNull(uncheckedIoException.getCause());
        assertInstanceOf(UncheckedIOException.class, uncheckedIoException);
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowsUncheckedIOExceptionWhenWrappedIOExceptionIsThrown() throws IOException {
        throw new IOException("Test IO Exception");
    }
}