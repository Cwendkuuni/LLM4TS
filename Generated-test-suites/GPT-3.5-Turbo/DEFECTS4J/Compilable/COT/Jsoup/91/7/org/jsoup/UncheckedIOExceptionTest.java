package org.jsoup;
import org.junit.Test;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testIoException() {
        IOException exception = new IOException("IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        
        assert(uncheckedException.ioException() instanceof IOException);
    }
}