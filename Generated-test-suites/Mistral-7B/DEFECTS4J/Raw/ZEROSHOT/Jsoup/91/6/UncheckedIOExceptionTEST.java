package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException cause = new IOException("Expected IOException message");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        Assert.assertEquals("Expected IOException message", uncheckedException.getMessage());
        Assert.assertSame(cause, uncheckedException.getCause());
    }

    @Test(expected = UncheckedIOException.class)
    public void testUncheckedIOExceptionThrowsExpectedException() throws IOException {
        IOException cause = new IOException("Expected IOException message");
        throw new UncheckedIOException(cause);
    }

    @Test
    public void testIoExceptionMethod() {
        IOException cause = new IOException("Expected IOException message");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedException.ioException());
    }

    @Test
    public void testToStringMethod() {
        IOException cause = new IOException("Expected IOException message");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        String expectedToString = String.format("UncheckedIOException: %s", cause.getMessage());
        Assert.assertEquals(expectedToString, uncheckedException.toString());
    }
}