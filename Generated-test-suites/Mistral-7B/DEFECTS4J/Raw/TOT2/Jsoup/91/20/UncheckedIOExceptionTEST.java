import org.junit.Test;
import org.junit.Assert;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Unexpected error occurred.");
        UncheckedIOException e = new UncheckedIOException(cause);
        Assert.assertEquals(cause, e.ioException());
    }
}