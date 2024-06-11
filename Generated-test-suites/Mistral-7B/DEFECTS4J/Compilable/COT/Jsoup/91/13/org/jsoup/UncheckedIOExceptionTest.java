package org.jsoup;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetMessage() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        String expectedMessage = "ioException = Test IOException";
        String actualMessage = uncheckedIOException.getMessage();

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testIOExceptionIsNotNull() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);

        Assert.assertNotNull(uncheckedIOException.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testIOExceptionIsNullThrowsNullPointerException() {
        new UncheckedIOException(null);
    }

    @Test
    public void testToString() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        String expectedString = "UncheckedIOException: ioException = Test IOException";
        String actualString = uncheckedIOException.toString();

        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetCauseReturnsOriginalIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testGetCauseIsWrapperException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException", new IOException("Test InnerIOException"))) {
            @Override
            public IOException getCause() {
                return (IOException) super.getCause();
            }
        };

        Assert.assertSame(ioException, uncheckedIOException.getCause());
        Assert.assertNotNull(uncheckedIOException.getCause().getCause());
        Assert.assertEquals("Test InnerIOException", uncheckedIOException.getCause().getCause().getMessage());
    }
}