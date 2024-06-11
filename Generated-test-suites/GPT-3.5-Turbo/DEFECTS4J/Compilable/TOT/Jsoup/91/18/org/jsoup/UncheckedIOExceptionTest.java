package org.jsoup;
import org.junit.Test;
import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.net.SocketException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        // Assert that the cause of the UncheckedIOException is the provided exception
        assertEquals(exception, uncheckedIOException.getCause());
    }

    @Test
    public void testIOExceptionMethod() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        // Assert that the IOException returned by ioException() method is the same as the one passed in the constructor
        assertEquals(exception, uncheckedIOException.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullCause() {
        new UncheckedIOException(null);
    }

    @Test(expected = SocketException.class)
    public void testIOExceptionMethodWithDifferentException() {
        IOException exception = new SocketException("Socket Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        // Assert that the IOException returned by ioException() method is the same as the one passed in the constructor, even if it is a different subclass
        assertEquals(exception, uncheckedIOException.ioException());
    }

    @Test
    public void testIOExceptionMethodWithNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        // Assert that the IOException returned by ioException() method is null when the cause is null
        assertEquals(null, uncheckedIOException.ioException());
    }

    @Test
    public void testCauseMessage() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        // Assert that the message of the exception returned by getCause() matches the message of the provided exception
        assertEquals(exception.getMessage(), uncheckedIOException.getCause().getMessage());
    }

    @Test
    public void testCauseType() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        // Assert that the type of the exception returned by getCause() is IOException
        assertTrue(uncheckedIOException.getCause() instanceof IOException);
    }

    @Test
    public void testCauseInstance() {
        IOException exception = new IOException("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);

        // Assert that the instance of the exception returned by getCause() is the same as the provided exception instance
        assertEquals(exception, uncheckedIOException.getCause());
    }

}