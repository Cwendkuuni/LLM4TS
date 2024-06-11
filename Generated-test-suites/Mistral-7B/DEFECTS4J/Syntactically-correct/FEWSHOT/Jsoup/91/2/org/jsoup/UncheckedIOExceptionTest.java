package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException ioException = new IOException("Some error message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
    }

    @Test
    public void testGetCauseReturnsOriginalIOException() {
        IOException ioException = new IOException("Some error message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIOException.getCause());
    }

    @Test
    public void testConstructWithNullIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }
}