package org.junit;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionMethod() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testThrowingUncheckedIOException() {
        try {
            throw new UncheckedIOException(new IOException("Expected IOException"));
        } catch (UncheckedIOException e) {
            Assert.assertEquals("Expected IOException", e.getMessage());
        }
    }
}