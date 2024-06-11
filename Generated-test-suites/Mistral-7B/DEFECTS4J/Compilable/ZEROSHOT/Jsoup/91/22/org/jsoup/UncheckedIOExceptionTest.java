package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionCreation() {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        Assert.assertSame(cause, uncheckedIOException.ioException());
        Assert.assertEquals("Expected IOException", uncheckedIOException.getMessage());
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowsUncheckedIOException() throws IOException {
        IOException cause = new IOException("Expected IOException");
        throw new UncheckedIOException(cause);
    }

    @Test
    public void testGetCauseReturnsIoException() {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    @Test
    public void testToStringMethod() {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        String expectedToString = "org.jsoup.UncheckedIOException: Expected IOException";
        String actualToString = uncheckedIOException.toString();

        Assert.assertEquals(expectedToString, actualToString);
    }
}