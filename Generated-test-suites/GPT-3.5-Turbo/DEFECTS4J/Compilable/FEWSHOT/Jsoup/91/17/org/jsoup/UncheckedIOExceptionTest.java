
package org.jsoup;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {

    private UncheckedIOException uncheckedIOException;

    @Before
    public void setUp() {
        IOException ioException = new IOException("Test Message");
        uncheckedIOException = new UncheckedIOException(ioException);
    }

    @Test
    public void testUncheckedIOException() {
        IOException cause = uncheckedIOException.ioException();
        assertEquals("Test Message", cause.getMessage());
    }
}
