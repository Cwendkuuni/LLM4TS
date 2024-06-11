import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(input, output);
        assertEquals(inputBytes.length, bytesCopied);

        byte[] outputBytes = output.toByteArray();
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(input, output, 5);
        assertEquals(inputBytes.length, bytesCopied);

        byte[] outputBytes = output.toByteArray();
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        long bytesSkipped = IOUtils.skip(input, 5);
        assertEquals(5, bytesSkipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[inputBytes.length];

        int bytesRead = IOUtils.readFully(input, outputBytes);
        assertEquals(inputBytes.length, bytesRead);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[inputBytes.length];

        int bytesToRead = 4;
        int offset = 4;
        int bytesRead = IOUtils.readFully(input, outputBytes, offset, bytesToRead);
        assertEquals(bytesToRead, bytesRead);
        assertArrayEquals("Input".getBytes(), outputBytes);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(input);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCloseQuietly() {
        // Test closing a Closeable (e.g. ByteArrayOutputStream) without exception
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.closeQuietly(output);
        
        // Test closing a null Closeable without exception
        IOUtils.closeQuietly(null);
    }

}