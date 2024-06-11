import org.junit.Test;
import java.io.*;
import static org.junit.Assert.*;

public class IOUtilsTest {

    private final InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        long count = IOUtils.copy(input, output);
        assertEquals(13, count);
        assertEquals("Hello, World!", output.toString());
    }

    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        byte[] data = "Custom buffer size test".getBytes();
        InputStream customInput = new ByteArrayInputStream(data);
        ByteArrayOutputStream customOutput = new ByteArrayOutputStream();
        long count = IOUtils.copy(customInput, customOutput, data.length);
        assertEquals(data.length, count);
        assertEquals("Custom buffer size test", customOutput.toString());
        customInput.close();
        customOutput.close();
    }

    @Test
    public void testCopyWithZeroInputBytes() throws IOException {
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        OutputStream emptyOutput = new ByteArrayOutputStream();
        long count = IOUtils.copy(emptyInput, emptyOutput);
        assertEquals(0, count);
        assertEquals("", emptyOutput.toString());
        emptyInput.close();
        emptyOutput.close();
    }

    @Test
    public void testSkipWithValidBytes() throws IOException {
        long count = IOUtils.skip(input, 7);
        assertEquals(7, count);
        assertEquals("World!", inputToString(input));
    }

    @Test
    public void testSkipWithZeroBytes() throws IOException {
        long count = IOUtils.skip(input, 0);
        assertEquals(0, count);
        assertEquals("Hello, World!", inputToString(input));
    }

    @Test
    public void testSkipWithInvalidBytes() throws IOException {
        long count = IOUtils.skip(input, 20);
        assertEquals(13, count);
        assertEquals("", inputToString(input));
    }

    @Test
    public void testReadFullyWithExactBytes() throws IOException {
        byte[] buffer = new byte[5];
        int count = IOUtils.readFully(input, buffer);
        assertEquals(5, count);
        assertEquals("Hello", new String(buffer));
    }

    @Test
    public void testReadFullyWithMoreBytes() throws IOException {
        byte[] buffer = new byte[20];
        int count = IOUtils.readFully(input, buffer);
        assertEquals(13, count);
        assertEquals("Hello, World!", new String(buffer, 0, count));
    }

    @Test
    public void testReadFullyWithInvalidOffset() {
        byte[] buffer = new byte[5];
        try {
            int count = IOUtils.readFully(input, buffer, 2, 4);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Invalid index", e.getMessage());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadFullyWithInvalidLength() {
        byte[] buffer = new byte[5];
        try {
            int count = IOUtils.readFully(input, buffer, 0, 10);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Invalid length", e.getMessage());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] byteArray = IOUtils.toByteArray(input);
        assertArrayEquals("Hello, World!".getBytes(), byteArray);
    }

    private String inputToString(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output);
        return output.toString();
    }
}