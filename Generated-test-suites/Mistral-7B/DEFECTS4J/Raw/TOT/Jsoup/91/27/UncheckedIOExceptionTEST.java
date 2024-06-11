
package org.jsoup.test; // Add this line to the test file to ensure package visibility

import org.junit.Test;
import org.jsoup.UncheckedIOException;
import java.io.IOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        // Assert that the UncheckedIOException instance holds the correct IOException cause
        assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        // Assert that the ioException() method returns the correct IOException instance
        assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUncheckedIOExceptionConstructionWithNullIOException() {
        IOException ioException = null;
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        // Assert that a NullPointerException is thrown when the IOException is null
        assertNull(uncheckedIOException);
    }

    @Test(expected = ClassCastException.class)
    public void testIoExceptionMethodWithDifferentExceptionType() {
        RuntimeException runtimeException = new RuntimeException("Test RuntimeException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(runtimeException);

        // Assert that the ioException() method throws a ClassCastException when the cause is not an IOException
        uncheckedIOException.ioException();
    }
}