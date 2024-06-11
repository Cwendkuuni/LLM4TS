import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertThat(uncheckedIOException, instanceOf(UncheckedIOException.class));
        assertThat(uncheckedIOException.ioException(), instanceOf(IOException.class));
        assertThat(uncheckedIOException.ioException().getMessage(), equalTo("Test IOException"));
    }

    @Test
    public void testGetCause() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionThrowsCorrectException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = null;

        try {
            uncheckedIOException = new UncheckedIOException(ioException);
        } catch (RuntimeException e) {
            uncheckedIOException = (UncheckedIOException) e;
        }

        assertNull(uncheckedIOException);
        assertThrows(IOException.class, () -> uncheckedIOException.ioException());
    }
}