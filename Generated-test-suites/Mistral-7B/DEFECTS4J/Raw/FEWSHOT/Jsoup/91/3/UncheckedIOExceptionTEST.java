package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionInstanceOfIOException() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test
    public void testNullIOExceptionThrowsNullPointerException() {
        assertNull(new UncheckedIOException(null));
    }

    @Test(expected = NullPointerException.class)
    public void testEmptyConstructorThrowsNullPointerException() {
        new UncheckedIOException();
    }
}