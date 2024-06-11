package org.jsoup;
import org.jsoup.UncheckedIOException;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException exception = new UncheckedIOException(cause);
        assertEquals("The cause is not as expected", cause, exception.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testUncheckedIOExceptionConstructorWithNull() {
        new UncheckedIOException(null);
    }

    @Test
    public void testIOExceptionGetter() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException exception = new UncheckedIOException(cause);
        assertNotNull("IOException instance was null", exception.ioException());
    }
}