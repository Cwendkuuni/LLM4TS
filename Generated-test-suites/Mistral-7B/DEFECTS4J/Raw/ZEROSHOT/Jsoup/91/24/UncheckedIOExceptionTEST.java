package org.jsoup.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.jsoup.UncheckedIOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionInstance() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertNotNull(uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testGetCauseUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        uncheckedIOException.ioException(); // this line will throw IOException
    }

    @Test
    public void testConstructors() {
        String message = "Test Message";
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(message, ioException);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(ioException);

        assertInstanceOf(UncheckedIOException.class, uncheckedIOException1);
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException2);
        assertInstanceOf(IOException.class, uncheckedIOException1.ioException());
        assertInstanceOf(IOException.class, uncheckedIOException2.ioException());
        assertNotNull(uncheckedIOException1.getMessage());
        assertNotNull(uncheckedIOException2.getMessage());
        assertNotNull(uncheckedIOException1.getMessage());
        assertNotNull(uncheckedIOException2.getMessage());
        assertNotNull(uncheckedIOException1.ioException());
        assertNotNull(uncheckedIOException2.ioException());
        assertNotSame(ioException, uncheckedIOException1.ioException());
        assertNotSame(ioException, uncheckedIOException2.ioException());
    }

}