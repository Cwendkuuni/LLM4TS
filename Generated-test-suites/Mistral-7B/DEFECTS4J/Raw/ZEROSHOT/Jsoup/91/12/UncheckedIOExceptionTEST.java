package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithCause() {
        IOException ioException = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertTrue(uncheckedIOException instanceof UncheckedIOException);
        assertTrue(uncheckedIOException instanceof RuntimeException);
        assertTrue(uncheckedIOException instanceof IOException);
        assertEquals(ioException.getMessage(), uncheckedIOException.getMessage());
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGettingIOExceptionFromUncheckedIOException() {
        IOException ioException = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        IOException actualIOException = uncheckedIOException.ioException();
        assertEquals(ioException, actualIOException);
    }

    @Test(expected = IOException.class)
    public void testThrowingIOExceptionFromUncheckedIOExceptionWithWrongType() {
        IOException ioException = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        uncheckedIOException.throwIOException(); // UncheckedIOException does not have this method
    }

    @Test(expected = RuntimeException.class)
    public void testThrowingRuntimeExceptionFromUncheckedIOException() {
        IOException ioException = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        uncheckedIOException.throwRuntimeException(); // UncheckedIOException does not have this method
    }

    @Test
    public void testConstructingUncheckedIOExceptionWithNullCause() {
        try {
            new UncheckedIOException(null);
            fail("Expected IllegalArgumentException when passing null as a cause");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}