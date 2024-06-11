package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionInstance() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCause() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    @Test(expected = IOException.class)
    public void testThrowOriginalIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.ioException();
    }

    @Test
    public void testSetSuppressedException() {
        IOException cause = new IOException("Test IOException 1");
        IOException suppressedException = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.addSuppressed(suppressedException);
        Assert.assertArrayEquals(new IOException[] { cause, suppressedException }, uncheckedIOException.getSuppressed());
    }

    @Test
    public void testThrowUncheckedIOExceptionWithNullCause() {
        Assert.expectThrows(NullPointerException.class, () -> new UncheckedIOException(null));
    }

    @Test
    public void testToString() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String toString = uncheckedIOException.toString();
        String expected = "UncheckedIOException: Test IOException";
        Assert.assertEquals(expected, toString);
    }

    @Test
    public void testHasRootCause() {
        IOException cause = new IOException("Test IOException 1");
        IOException rootCause = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, rootCause);
        Assert.assertFalse(uncheckedIOException.hasRootCause());
        cause.initCause(rootCause);
        Assert.assertTrue(uncheckedIOException.hasRootCause());
    }

    @Test
    public void testGetRootCause() {
        IOException cause = new IOException("Test IOException 1");
        IOException rootCause = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, rootCause);
        Assert.assertNull(uncheckedIOException.getRootCause());
        cause.initCause(rootCause);
        Assert.assertSame(rootCause, uncheckedIOException.getRootCause());
    }
}