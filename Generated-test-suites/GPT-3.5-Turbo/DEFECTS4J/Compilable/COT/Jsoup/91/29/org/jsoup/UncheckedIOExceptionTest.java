package org.jsoup;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testIoException() {
        IOException ioException = new IOException("test");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    // Potential edge cases and exception handling scenarios
    // 1. Test with null IOException
    // 2. Test with different types of IOExceptions
    // 3. Test with chained exceptions

    @Test
    public void testIoException_NullException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testIoException_DifferentIOExceptions() {
        IOException ioException1 = new IOException("test1");
        IOException ioException2 = new IOException("test2");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(ioException1);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(ioException2);
        assertNotEquals(ioException1, uncheckedIOException2.ioException());
        assertNotEquals(ioException2, uncheckedIOException1.ioException());
    }

    @Test
    public void testIoException_ChainedExceptions() {
        IOException ioException1 = new IOException("test1");
        IOException ioException2 = new IOException("test2", ioException1);
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException2);
        assertEquals(ioException2, uncheckedIOException.ioException());
    }

}