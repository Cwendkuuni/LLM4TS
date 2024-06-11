package org.jsoup;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import org.jsoup.UncheckedIOException;

public class UncheckedIOExceptionTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.getCause());
    }

    // Test case for ioException() method
    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        IOException returnedException = uncheckedIOException.ioException();

        assertEquals(ioException, returnedException);
    }
}