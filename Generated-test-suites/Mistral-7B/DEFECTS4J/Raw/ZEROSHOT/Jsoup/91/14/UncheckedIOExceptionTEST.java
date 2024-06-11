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
        Assert.assertSame(cause, uncheckedIOException.ioException());
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
    }

    @Test(expected = IOException.class)
    public void testIoExceptionThrownWhenAccessingUncheckedIOExceptionCause() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.ioException();
    }

    @Test
    public void testGetMessage() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
    }

    @Test
    public void testEquals() {
        IOException cause1 = new IOException("Test IOException 1");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(cause1);

        IOException cause2 = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(cause2);

        Assert.assertNotEquals(uncheckedIOException1, uncheckedIOException2);

        UncheckedIOException sameInstance = new UncheckedIOException(cause1);
        Assert.assertEquals(uncheckedIOException1, sameInstance);
    }

    @Test
    public void testToString() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expected = "UncheckedIOException: Test IOException";
        Assert.assertEquals(expected, uncheckedIOException.toString());
    }
}