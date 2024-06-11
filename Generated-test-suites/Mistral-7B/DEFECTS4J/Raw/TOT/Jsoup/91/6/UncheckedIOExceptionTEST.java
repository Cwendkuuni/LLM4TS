import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.assertInstanceOf;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructWithIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    
    // Your complete test file should include the following tests:
    // Add test cases for edge cases, typical use cases, and error scenarios

    @Test
    public test_edge_case_1() {
        // Edge case 1 test
    }

    @Test
    public test_edge_case_2() {
        // Edge case 2 test
    }

    @Test
    public test_typical_use_case() {
        // Typical use case test
    }

    @Test(expected = UncheckedIOException.class)
    public test_error_scenario() {
        // Error scenario test
    }

    
}