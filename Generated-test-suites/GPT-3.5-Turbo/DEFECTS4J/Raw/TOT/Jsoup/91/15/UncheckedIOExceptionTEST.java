
package org.jsoup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UncheckedIOExceptionTest {

    @Mock
    private IOException mockCause;

    @Test
    public void testConstructorAndGetCause() {
        UncheckedIOException exception = new UncheckedIOException(mockCause);
        assertEquals(mockCause, exception.getCause());
    }

    @Test
    public void testIOException() {
        UncheckedIOException exception = new UncheckedIOException(mockCause);
        assertEquals(mockCause, exception.ioException());
    }
}

