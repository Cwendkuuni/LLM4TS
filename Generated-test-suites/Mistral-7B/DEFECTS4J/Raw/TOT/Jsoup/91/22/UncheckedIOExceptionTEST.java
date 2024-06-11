
package org.jsoup.test; // Add this package declaration for the test class

import org.junit.Test;
import org.jsoup.UncheckedIOException;
import java.io.IOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("Test IOException", uncheckedIOException.getMessage());
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertTrue(uncheckedIOException.ioException() instanceof IOException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionWithNullArgument() {
        assertThrows(NullPointerException.class, () -> new UncheckedIOException(null));
    }

    @Test
    public void testUncheckedIOExceptionWithIOExceptionArgument() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertNotNull(uncheckedIOException);
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionWithNonIOExceptionArgument() {
        Assertions.assertThrows(ClassCastException.class, () -> new UncheckedIOException(new RuntimeException("Test RuntimeException")));
    }
}