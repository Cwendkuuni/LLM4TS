package org.jsoup;

import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() throws IOException {
        IOException cause = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        assertEquals(cause, uncheckedIOException.ioException());
    }

}