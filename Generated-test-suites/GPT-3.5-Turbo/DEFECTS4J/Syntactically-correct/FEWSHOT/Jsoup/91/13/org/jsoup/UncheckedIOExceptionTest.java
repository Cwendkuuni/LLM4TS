// JUnit 4 Test Cases for class UncheckedIOException

package org.jsoup;

import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("IO Exception");
        UncheckedIOException uncheckedIoException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIoException.ioException());
    }

    @Test
    public void testIOException() {
        IOException ioException = new IOException("IO Exception");
        UncheckedIOException uncheckedIoException = new UncheckedIOException(ioException);
        assertEquals(ioException, uncheckedIoException.ioException());
    }

}