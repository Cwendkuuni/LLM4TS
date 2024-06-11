package org.jsoup;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException cause = new IOException("Exception message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Exception message", uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException cause = new IOException("Exception message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    // Edge cases and exception handling scenarios:
    // 1. Null IOException cause
    @Test(expected = NullPointerException.class)
    public void testUncheckedIOExceptionWithNullCause() {
        new UncheckedIOException(null);
    }

    // 2. Test getMessage() when IOException cause has a message
    @Test
    public void testGetMessageWithIoExceptionMessage() {
        IOException cause = new IOException("Exception message from IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Exception message from IOException", uncheckedIOException.getMessage());
    }

    // 3. Test getMessage() when IOException cause has no message
    @Test
    public void testGetMessageWithNoIoExceptionMessage() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals(UncheckedIOException.class.getSimpleName(), uncheckedIOException.getMessage());
    }

    // 4. Test ioException() when IOException cause has a message
    @Test
    public void testIoExceptionWithIoExceptionMessage() {
        IOException cause = new IOException("Exception message from IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Exception message from IOException", uncheckedIOException.ioException().getMessage());
    }

    // 5. Test ioException() when IOException cause has no message
    @Test
    public void testIoExceptionWithNoIoExceptionMessage() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertNull(uncheckedIOException.ioException().getMessage());
    }
}