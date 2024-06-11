package org.jsoup;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithCause() {
        IOException ioException = new IOException("expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertNotNull(uncheckedIOException.getCause());
        assertInstanceOf(IOException.class, uncheckedIOException.getCause());
        assertEquals("expected IOException message", uncheckedIOException.getMessage());
    }

    @Test(expected = IOException.class)
    public void testThrowingUncheckedIOExceptionFromMethod() {
        try {
            throw new UncheckedIOException(new IOException("expected IOException message in test case"));
        } catch (RuntimeException e) {
            assertInstanceOf(UncheckedIOException.class, e);
            assertInstanceOf(IOException.class, e.ioException());
            throw e.getCause();
        }
    }
}