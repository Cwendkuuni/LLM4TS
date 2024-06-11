package org.jsoup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    private UncheckedIOException uncheckedIOException;

    @Before
    public void setUp() {
        IOException ioException = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(ioException);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(uncheckedIOException);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertEquals(IOException.class, uncheckedIOException.getCause().getClass());
    }

    @Test
    public void testIoException() {
        IOException cause = uncheckedIOException.ioException();
        Assert.assertNotNull(cause);
        Assert.assertEquals("Test IOException", cause.getMessage());
    }
}