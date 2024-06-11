
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopyNullInputStream() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        assertNull(IOUtils.copy(null, output));
    }

    // Test other edge cases and exception handling scenarios here

    @Test
    public void testCopyNullOutputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        assertNull(IOUtils.copy(input, null));
    }

    // Test other test cases here

    @Test
    public void testCopySameInputStreamAndOutputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        assertEquals(input.available(), actual);
        assertEquals("Test String", new String(output.toByteArray()));

        // Reset input and output streams
        input.reset();
        output.reset();

        long actual2 = IOUtils.copy(input, output);
        assertEquals(0, actual2);
    }

    // Test other test cases here
}
