import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class IOUtilsTest {
    
    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(13, count);
        assertEquals("Hello, World!", output.toString());
    }
    
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 5);
        assertEquals(13, count);
        assertEquals("Hello, World!", output.toString());
    }
    
    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        long skipped = IOUtils.skip(input, 7);
        assertEquals(7, skipped);
        assertEquals('W', input.read());
    }
    
    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(5, bytesRead);
        assertEquals("Hello", new String(buffer));
    }
    
    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] byteArray = IOUtils.toByteArray(input);
        assertEquals("Hello, World!", new String(byteArray));
    }
    
    @Test
    public void testCloseQuietly() {
        Closeable c = null;
        // No exception should be thrown
        IOUtils.closeQuietly(c);
        
        c = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        // No exception should be thrown
        IOUtils.closeQuietly(c);
    }
}