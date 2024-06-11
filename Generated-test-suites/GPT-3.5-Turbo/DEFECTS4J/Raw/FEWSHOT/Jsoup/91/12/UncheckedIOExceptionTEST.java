package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException cause = new IOException("test");
        UncheckedIOException uioe = new UncheckedIOException(cause);
        assertEquals(cause, uioe.ioException());
    }
}