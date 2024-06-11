
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Closeable;
import org.apache.commons.compress.utils.IOUtils;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] data = "Test data".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        
        assertEquals(data.length, count);
        assertArrayEquals(data, output.toByteArray());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] data = "Test data".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bufferSize = 10;
        long count = IOUtils.copy(input, output, bufferSize);
        
        assertEquals(data.length, count);
        assertArrayEquals(data, output.toByteArray());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = "Test data".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        long numToSkip = 4;
        long skipped = IOUtils.skip(input, numToSkip);
        
        assertEquals(numToSkip, skipped);
        
        input.close();
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] data = "Test data".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        byte[] buffer = new byte[data.length];
        int bytesRead = IOUtils.readFully(input, buffer);
        
        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, buffer);
        
        input.close();
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] data = "Test data".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        int offset = 0;
        int length = 4;
        byte[] buffer = new byte[length];
        int bytesRead = IOUtils.readFully(input, buffer, offset, length);
        
        assertEquals(length, bytesRead);
        assertArrayEquals("Test".getBytes(), buffer);
        
        input.close();
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] data = "Test data".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        byte[] byteArray = IOUtils.toByteArray(input);
        
        assertArrayEquals(data, byteArray);
        
        input.close();
    }
    
    @Test
    public void testCloseQuietly() throws IOException {
        Closeable c = new Closeable() {
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
        IOUtils.closeQuietly(c);
        
        // No assertion as the method should execute without throwing an exception
    }
}
