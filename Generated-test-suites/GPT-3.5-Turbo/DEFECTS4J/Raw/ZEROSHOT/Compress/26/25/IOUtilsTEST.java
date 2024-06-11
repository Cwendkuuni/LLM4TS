import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy_defaultBufferSize() throws IOException {
        byte[] inputBytes = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);
        
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }
    
    @Test
    public void testCopy_customBufferSize() throws IOException {
        byte[] inputBytes = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();

        int bufferSize = 16;
        long count = IOUtils.copy(input, output, bufferSize);
        
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);

        long numToSkip = 7;
        long skipped = IOUtils.skip(input, numToSkip);
        
        assertEquals(numToSkip, skipped);
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[5];

        int bytesRead = IOUtils.readFully(input, buffer);
        
        assertEquals(buffer.length, bytesRead);
        assertArrayEquals(new byte[] {'H', 'e', 'l', 'l', 'o'}, buffer);
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputBytes = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[5];

        int bytesRead = IOUtils.readFully(input, buffer, 1, 3);
        
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[] {0, 'H', 'e', 'l', 0}, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCloseQuietly() {
        // Test for null Closeable object
        IOUtils.closeQuietly(null);
        
        // Test for valid Closeable object
        // TODO: Implement the test logic
        // Add code here...
    }
}