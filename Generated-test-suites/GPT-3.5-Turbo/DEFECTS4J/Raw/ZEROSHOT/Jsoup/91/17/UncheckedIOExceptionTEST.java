import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UncheckedIOExceptionTest {

    private UncheckedIOException uncheckedIOException;

    @Test
    public void testIOException() throws IOException {
        IOException exception = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(exception);

        assertEquals(exception, uncheckedIOException.ioException());
    }

}