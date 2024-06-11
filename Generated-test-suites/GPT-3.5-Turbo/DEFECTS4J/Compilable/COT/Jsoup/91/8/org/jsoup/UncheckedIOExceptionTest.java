package org.jsoup;
import org.jsoup.*;
import org.junit.*;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    private UncheckedIOException uncheckedIOException;

    @Before
    public void setUp() {
        IOException ioException = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(ioException);
    }

    @Test
    public void testIoException() {
        IOException expected = new IOException("Test IOException");
        IOException actual = uncheckedIOException.ioException();

        Assert.assertEquals(expected, actual);
    }

    @After
    public void tearDown() {
        uncheckedIOException = null;
    }
}