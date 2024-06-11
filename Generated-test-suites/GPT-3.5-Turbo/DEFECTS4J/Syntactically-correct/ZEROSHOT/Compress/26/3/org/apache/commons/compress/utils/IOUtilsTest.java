
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        byte[] testData = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(testData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        assertEquals(testData.length, count);
        assertArrayEquals(testData, output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws Exception {
        byte[] testData = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(testData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        int bufferSize = 1024;
        long count = IOUtils.copy(input, output, bufferSize);
        
        assertEquals(testData.length, count);
        assertArrayEquals(testData, output.toByteArray());
    }

    @Test
    public void testSkip() throws Exception {
        byte[] testData = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(testData);
        
        long numToSkip = 7;
        long skipped = IOUtils.skip(input, numToSkip);
        
        assertEquals(numToSkip, skipped);
        assertEquals('W', input.read());
    }

    @Test
    public void testReadFully() throws Exception {
        byte[] testData = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(testData);
        
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);
        
        assertEquals(buffer.length, bytesRead);
        assertArrayEquals(new byte[] {'H', 'e', 'l', 'l', 'o'}, buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws Exception {
        byte[] testData = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(testData);
        
        byte[] buffer = new byte[10];
        int offset = 2;
        int length = 5;
        int bytesRead = IOUtils.readFully(input, buffer, offset, length);
        
        assertEquals(length, bytesRead);
        assertArrayEquals(new byte[] {0, 0, 'H', 'e', 'l', 'l', 'o', 0, 0, 0}, buffer);
    }

    @Test
    public void testToByteArray() throws Exception {
        byte[] testData = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(testData);
        
        byte[] result = IOUtils.toByteArray(input);
        
        assertArrayEquals(testData, result);
    }

    @Test
    public void testCloseQuietly() throws Exception {
        // Just testing that the method can run without errors
        IOUtils.closeQuietly(null);
    }
}
