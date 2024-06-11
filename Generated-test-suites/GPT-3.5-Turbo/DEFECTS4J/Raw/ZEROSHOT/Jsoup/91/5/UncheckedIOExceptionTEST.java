package org.jsoup;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals(ioException, uncheckedIOException.ioException());
    }

}