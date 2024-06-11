package org.jsoup;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testUncheckedIOException_NullPointerException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    }
}