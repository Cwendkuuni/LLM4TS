package org.jsoup;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class UncheckedIOExceptionTest {
    
    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Test exception");
        UncheckedIOException exception = new UncheckedIOException(cause);
        
        Assert.assertEquals(cause, exception.getCause());
    }
    
    @Test
    public void testIOException() {
        IOException cause = new IOException("Test exception");
        UncheckedIOException exception = new UncheckedIOException(cause);
        
        Assert.assertEquals(cause, exception.ioException());
    }
}