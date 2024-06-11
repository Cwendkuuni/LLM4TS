package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        assertEquals("Expected IOException", uncheckedIOException.getMessage());
        assertEquals(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionConstructorWithNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionConstructorWithEmptyMessageCause() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertEquals("", uncheckedIOException.getMessage());
        assertEquals(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionConstructorWithCustomMessageCause() {
        IOException cause = new IOException("Custom message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertEquals("Custom message", uncheckedIOException.getMessage());
        assertEquals(cause, uncheckedIOException.ioException());
    }
}