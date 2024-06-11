package org.jsoup;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionRetrieval() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException.getClass(), uncheckedIOException.ioException().getClass());
        Assert.assertEquals(ioException.getMessage(), uncheckedIOException.ioException().getMessage());
    }

    @Test
    public void testNormalOperation() {
        try {
            // Call a method that does not throw an IOException here
        } catch (UncheckedIOException e) {
            Assert.fail("Unexpected UncheckedIOException.");
        }
    }

    @Test(expected = IOException.class)
    public void testIoExceptionIsPropagated() throws IOException {
        IOException ioException = new IOException();
        throw new UncheckedIOException(ioException);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCauseThrowsNPE() {
        try {
            new UncheckedIOException(null);
            Assert.fail("Expected NullPointerException.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyMessageCause() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertNull(uncheckedIOException.getMessage());
    }

    @Test
    public void testMessageCause() {
        String message = "Test Message";
        IOException ioException = new IOException(message);
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals(message, uncheckedIOException.getMessage());
    }
}