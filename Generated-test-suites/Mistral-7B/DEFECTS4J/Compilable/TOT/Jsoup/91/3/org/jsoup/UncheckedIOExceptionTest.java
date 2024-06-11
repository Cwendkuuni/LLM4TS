package org.jsoup;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
        Assert.assertNotNull(uncheckedIOException.getCause());
    }
}