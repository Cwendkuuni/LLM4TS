package org.jsoup.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIoException = new UncheckedIOException(cause);
        assertInstanceOf(IOException.class, uncheckedIoException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedIoException);
    }

    @Test
    public void testNullIoException() {
        UncheckedIOException uncheckedIoException = new UncheckedIOException(null);
        assertNull(uncheckedIoException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIoExceptionThrown() throws IOException {
        IOException cause = new IOException("Expected IOException");
        throw new UncheckedIOException(cause);
    }
}