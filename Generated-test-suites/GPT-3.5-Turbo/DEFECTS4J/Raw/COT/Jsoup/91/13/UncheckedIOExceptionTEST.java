package org.jsoup;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOExceptionConstructor() {
        IOException ioException = new IOException("Some error message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCause() {
        IOException ioException = new IOException("Some error message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals(ioException, uncheckedIOException.getCause());
    }

}