import org.junit.Test;
import org.junit.Assert;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals(ioException, uncheckedIOException.ioException());
    }
}