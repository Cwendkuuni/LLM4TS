package org.jsoup;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException cause = new IOException();
        UncheckedIOException exception = new UncheckedIOException(cause);
        Assert.assertEquals(cause, exception.ioException());
    }
}