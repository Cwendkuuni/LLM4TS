package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("Test IO Exception", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCause() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testRethrowIOException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        throw uncheckedIOException;
    }

    @Test
    public void testNullIOException() {
        try {
            new UncheckedIOException(null);
            Assert.fail("Expected IOException but null was passed");
        } catch (NullPointerException e) {
            Assert.assertEquals("Null IOException was not expected", e.getMessage());
        }
    }
}