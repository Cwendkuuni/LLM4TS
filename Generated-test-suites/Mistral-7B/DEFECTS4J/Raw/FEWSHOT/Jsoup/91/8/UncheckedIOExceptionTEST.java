package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Test cause");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
        assertEquals("Test cause", uncheckedIOException.getMessage());
    }

    @Test
    public void testNullIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowUncheckedIOException() throws IOException {
        throw new UncheckedIOException(new IOException("Test cause"));
    }
}