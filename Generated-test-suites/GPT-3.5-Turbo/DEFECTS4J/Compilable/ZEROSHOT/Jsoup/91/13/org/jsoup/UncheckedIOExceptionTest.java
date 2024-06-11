package org.jsoup;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals(ioException, uncheckedIOException.ioException());
    }

}