import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UncheckedIOExceptionTest {

    @Mock
    private IOException mockIOException;

    @Test
    public void testConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(mockIOException);

        assertEquals(mockIOException, uncheckedIOException.ioException());
        assertTrue(uncheckedIOException.getMessage().contains("java.io.IOException"));
    }

    @Test
    public void testGetter() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(mockIOException);

        assertEquals(mockIOException, uncheckedIOException.ioException());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullParam() {
        new UncheckedIOException(null);
    }
}