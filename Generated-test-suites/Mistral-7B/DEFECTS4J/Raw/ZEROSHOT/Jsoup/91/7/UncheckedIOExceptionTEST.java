package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionInstance() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("Test IO Exception", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionConstructorWithMessage() {
        String message = "Test Unchecked IOException Message";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(message);
        Assert.assertEquals(message, uncheckedIOException.getMessage());
        Assert.assertNull(uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowUncheckedIOException() throws IOException {
        IOException ioException = new IOException("Test IO Exception");
        throw new UncheckedIOException(ioException);
    }

    @Test
    public void testGetCause() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testGetLocalizedMessage() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals(uncheckedIOException.getMessage(), ioException.getLocalizedMessage());
    }

    @Test
    public void testPrintStackTrace() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        uncheckedIOException.printStackTrace();
        ioException.printStackTrace();
        Assert.assertTrue("Stacktraces should be equal", stackTraceEquals(uncheckedIOException.getStackTrace(), ioException.getStackTrace()));
    }

    @Test
    public void testPrintStacktraceString() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        uncheckedIOException.printStackTrace(System.out);
        ioException.printStackTrace(System.out);
        Assert.assertTrue("Stacktraces should be equal", stackTraceEquals(uncheckedIOException.getStackTrace(), ioException.getStackTrace()));
    }

    private boolean stackTraceEquals(StackTraceElement[] stackTrace1, StackTraceElement[] stackTrace2) {
        if (stackTrace1.length != stackTrace2.length) {
            return false;
        }
        for (int i = 0; i < stackTrace1.length; ++i) {
            if (!stackTrace1[i].equals(stackTrace2[i])) {
                return false;
            }
        }
        return true;
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowUncheckedIOExceptionWithMessage() throws IOException {
        String message = "Test Unchecked IOException Message";
        throw new UncheckedIOException(message);
    }

    @Test
    public void testEquals() {
        IOException ioException1 = new IOException("Test IO Exception 1");
        IOException ioException2 = new IOException("Test IO Exception 2");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(ioException1);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(ioException2);
        Assert.assertNotEquals(uncheckedIOException1, uncheckedIOException2);
        Assert.assertNotEquals(uncheckedIOException1, ioException1);
        Assert.assertNotEquals(uncheckedIOException2, ioException2);
        Assert.assertNotEquals(uncheckedIOException1, new Object());
        UncheckedIOException uncheckedIOException3 = new UncheckedIOException(ioException1);
        Assert.assertEquals(uncheckedIOException1, uncheckedIOException3);
    }

    @Test
    public void testHashCode() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(ioException);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(ioException);
        Assert.assertEquals(uncheckedIOException1.hashCode(), uncheckedIOException2.hashCode());
        Assert.assertEquals(uncheckedIOException1.hashCode(), ioException.hashCode());
    }

    @Test
    public void testToString() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("UncheckedIOException: Test IO Exception", uncheckedIOException.toString());
    }
}