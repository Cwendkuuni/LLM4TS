import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {

    @Test
    public void testIOException() {
        IOException exception = new IOException("This is a test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        assertEquals(exception, uncheckedIOException.ioException());
    }

}