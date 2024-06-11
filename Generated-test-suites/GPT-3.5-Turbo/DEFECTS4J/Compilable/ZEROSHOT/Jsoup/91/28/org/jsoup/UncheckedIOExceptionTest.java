package org.jsoup;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException causeIOException = new IOException("Test exception");
        UncheckedIOException exception = new UncheckedIOException(causeIOException);
        assertNotNull(exception);
        assertEquals(causeIOException, exception.ioException());
    }

    @Test
    public void testIOException() {
        IOException causeIOException = new IOException("Test exception");
        UncheckedIOException exception = new UncheckedIOException(causeIOException);

        try {
            throw exception;
        } catch (UncheckedIOException e) {
            assertEquals(causeIOException, e.ioException());
        }
    }
}