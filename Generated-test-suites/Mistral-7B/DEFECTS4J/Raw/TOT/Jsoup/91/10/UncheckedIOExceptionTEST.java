package org.jsoup.junit;

import org.junit.Test;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;
import org.jsoup.UncheckedIOException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class UncheckedIOExceptionTest {
    private IOException ioException;
    private UncheckedIOException uncheckedIOException;

    public UncheckedIOExceptionTest(IOException ioException) {
        this.ioException = ioException;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new FileNotFoundException("Test File Not Found")},
                {new IOException("Test IOException")},
                {new NullPointerException("Test NullPointerException")}
        });
    }

    @Test
    public void testUncheckedIOExceptionConstruction() {
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionThrowsCorrectException() {
        uncheckedIOException = new UncheckedIOException(ioException);

        assertThrows(UncheckedIOException.class, () -> {
            throw uncheckedIOException;
        });
    }
}