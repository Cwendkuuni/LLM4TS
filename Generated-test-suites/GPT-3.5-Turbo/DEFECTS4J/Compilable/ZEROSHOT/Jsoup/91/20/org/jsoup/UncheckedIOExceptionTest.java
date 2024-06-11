package org.jsoup;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException ex = new UncheckedIOException(cause);
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testIoException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException ex = new UncheckedIOException(cause);
        assertSame(cause, ex.ioException());
    }

}