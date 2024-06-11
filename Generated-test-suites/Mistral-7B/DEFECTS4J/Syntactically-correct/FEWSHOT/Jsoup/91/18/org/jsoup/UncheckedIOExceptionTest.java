package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test
    public void testThrowUncheckedIOExceptionWhenWrappingIOException() {
        assertThrows(UncheckedIOException.class, () -> {
            throw new UncheckedIOException(new IOException("Test IOException"));
        });
    }

    @Test
    public void testIoExceptionIsRetrievedAfterWrapping() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        IOException actualIOException = uncheckedIOException.ioException();
        assertEquals(ioException.getMessage(), actualIOException.getMessage());
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowUncheckedIOExceptionWhenInvokingMethodThatThrowsIOException() throws IOException {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException"));
        uncheckedIOException.ioException(); // This line will throw UncheckedIOException
    }
}