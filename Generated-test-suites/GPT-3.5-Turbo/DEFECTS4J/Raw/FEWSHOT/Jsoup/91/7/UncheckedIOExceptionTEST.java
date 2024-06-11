package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException cause = mock(IOException.class);
        UncheckedIOException exception = new UncheckedIOException(cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testIoException() {
        IOException cause = mock(IOException.class);
        UncheckedIOException exception = new UncheckedIOException(cause);
        assertEquals(cause, exception.ioException());
    }
}