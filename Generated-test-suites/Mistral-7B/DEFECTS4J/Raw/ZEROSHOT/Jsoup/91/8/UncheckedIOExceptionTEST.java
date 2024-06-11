package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testGetCauseThrowsIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException"));
        uncheckedIOException.getCause();
    }

    @Test
    public void testToString() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expected = "org.jsoup.UncheckedIOException: Test IOException";
        String actual = uncheckedIOException.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testStacktrace() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expected = "java.io.IOException: Test IOException";
        String actual = uncheckedIOException.printStackTrace().toString();
        Assert.assertTrue(actual.contains(expected));
    }

    @Test
    public void testUncheckedIOExceptionWithCustomMessage() {
        String customMessage = "Test UncheckedIOException with custom message";
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(customMessage, cause);
        Assert.assertEquals(customMessage, uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testNestedIOException() {
        IOException outerCause = new IOException("Test outer IOException");
        IOException innerCause = new IOException("Test inner IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(outerCause);
        uncheckedIOException.initCause(new UncheckedIOException(innerCause));
        Assert.assertSame(innerCause, uncheckedIOException.ioException().getCause());
        Assert.assertSame(outerCause, uncheckedIOException.getCause());
    }
}