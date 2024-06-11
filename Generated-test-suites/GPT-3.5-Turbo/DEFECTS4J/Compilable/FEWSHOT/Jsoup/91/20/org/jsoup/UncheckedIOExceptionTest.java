package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UncheckedIOExceptionTest {
    @Test
    public void testIOExceptionConstructor() {
        IOException ioException = new IOException("Test exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(ioException);

        // Verify that the cause is set correctly
        assertEquals(ioException, uncheckedException.getCause());
    }

    @Test
    public void testIOExceptionGetter() {
        IOException ioException = new IOException("Test exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(ioException);

        // Verify that the getter returns the correct IOException instance
        assertEquals(ioException, uncheckedException.ioException());
    }
}