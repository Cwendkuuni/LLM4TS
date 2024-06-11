import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy_DefaultBufferSize() throws IOException {
        String inputString = "Test Input String";
        ByteArrayInputStream input = new ByteArrayInputStream(inputString.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output);
        
        assertEquals(inputString.length(), bytesCopied);
        assertEquals(inputString, output.toString());
    }
    
    @Test
    public void testCopy_CustomBufferSize() throws IOException {
        String inputString = "Test Input String";
        ByteArrayInputStream input = new ByteArrayInputStream(inputString.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bufferSize = 10;
        
        long bytesCopied = IOUtils.copy(input, output, bufferSize);
        
        assertEquals(inputString.length(), bytesCopied);
        assertEquals(inputString, output.toString());
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5, 6, 7, 8};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        long bytesSkipped = IOUtils.skip(input, 3);
        
        assertEquals(3, bytesSkipped);
        assertEquals(4, input.read());
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5, 6, 7, 8};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[8];
        
        int bytesRead = IOUtils.readFully(input, outputBytes);
        
        assertEquals(8, bytesRead);
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5, 6, 7, 8};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];
        
        int bytesRead = IOUtils.readFully(input, outputBytes, 1, 3);
        
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2, 3, 0}, outputBytes);
    }
    
    @Test
    public void testToByteArray() throws IOException {
        String inputString = "Test Input String";
        ByteArrayInputStream input = new ByteArrayInputStream(inputString.getBytes());
        
        byte[] byteArray = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputString.getBytes(), byteArray);
    }
    
    @Test
    public void testCloseQuietly() {
        Closeable c = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Fake IOException");
            }
        };
        
        IOUtils.closeQuietly(c);
        
        // No exception should be thrown
        assertTrue(true);
    }
}