package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertEquals("Test IOException", uncheckedIOException.getMessage());
        assertEquals(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testIOExceptionRetrieval() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testExceptionThrown() throws IOException {
        IOException cause = new IOException("Test IOException");
        throw new UncheckedIOException(cause);
    }

    @Test
    public void testToString() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expected = "org.jsoup.UncheckedIOException: Test IOException";
        String actual = uncheckedIOException.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetSuppressed() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Throwable suppressed = new Throwable("Test Suppressed");
        uncheckedIOException.setSuppressed(new Throwable[] { suppressed });
        Assert.assertArrayEquals(new Throwable[] { suppressed }, uncheckedIOException.getSuppressed());
    }
}