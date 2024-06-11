package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionCreation() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("Expected IOException", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCauseReturnsIoException() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowsUncheckedIOExceptionWhenGettingCause() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        try {
            Throwable cause = uncheckedIOException.getCause();
        } catch (IOException e) {
            // This should not be thrown
            Assert.fail("IOException should not be thrown when getting cause of UncheckedIOException");
        }
    }

    @Test
    public void testToString() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("UncheckedIOException: Expected IOException", uncheckedIOException.toString());
    }
}