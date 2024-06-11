package org.jsoup;
import org.junit.Test;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    @Test(expected = UncheckedIOException.class)
    public void testUncheckedIOExceptionConstruction() throws IOException {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
    }
}