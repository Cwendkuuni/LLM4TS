package org.jsoup;
import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testConstructor() {
        IOException exception = new IOException("I/O Exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assert(uncheckedException.ioException() == exception);
    }
    
    @Test
    public void testGetterReturnsCorrectIOException() {
        IOException exception = new IOException("I/O Exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assert(uncheckedException.ioException() == exception);
    }
    
    @Test
    public void testGetterReturnsExpectedType() {
        IOException exception = new IOException("I/O Exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(exception);
        assert(uncheckedException.ioException() instanceof IOException);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNullPointerException() {
        UncheckedIOException uncheckedException = new UncheckedIOException(null);
    }
    
    @Test
    public void testGetterReturnsNullWhenIOExceptionIsNull() {
        UncheckedIOException uncheckedException = new UncheckedIOException(null);
        assert(uncheckedException.ioException() == null);
    }
    
}