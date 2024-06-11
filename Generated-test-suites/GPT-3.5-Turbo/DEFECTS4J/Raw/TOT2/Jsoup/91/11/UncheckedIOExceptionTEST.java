
package org.jsoup;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException exception = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        assertNotNull(uncheckedIOException);
        assertEquals(exception, uncheckedIOException.ioException());
    }

}
