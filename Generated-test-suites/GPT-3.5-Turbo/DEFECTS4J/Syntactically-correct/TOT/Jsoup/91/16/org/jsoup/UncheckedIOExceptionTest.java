import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class UncheckedIOExceptionTest {

    private final IOException input;

    public UncheckedIOExceptionTest(IOException input) {
        this.input = input;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new IOException("Test IOException")},
                {null}
        });
    }

    @Test
    public void testConstructorAndGetCause() {
        UncheckedIOException exception = new UncheckedIOException(input);
        Assert.assertEquals(input, exception.getCause());
    }

    @Test
    public void testIOException() {
        UncheckedIOException exception = new UncheckedIOException(input);
        Assert.assertEquals(input, exception.ioException());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        UncheckedIOException exception = new UncheckedIOException(input);
        exception.printStackTrace();
    }
}