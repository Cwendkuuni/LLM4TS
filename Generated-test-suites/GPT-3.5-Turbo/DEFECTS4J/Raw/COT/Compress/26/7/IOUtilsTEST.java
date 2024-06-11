import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        int bufferSize = 3;
        long count = IOUtils.copy(input, output, bufferSize);
        
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        long numToSkip = 3;
        long skipped = IOUtils.skip(input, numToSkip);
        
        assertEquals(numToSkip, skipped);
        assertEquals(4, input.read());
        
        input.close();
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] b = new byte[5];
        int read = IOUtils.readFully(input, b);
        
        assertEquals(5, read);
        assertArrayEquals(inputBytes, b);
        
        input.close();
    }
    
    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] b = new byte[3];
        int offset = 1;
        int len = 3;
        int read = IOUtils.readFully(input, b, offset, len);
        
        assertEquals(len, read);
        byte[] expected = {0, 2, 3};
        assertArrayEquals(expected, b);
        
        input.close();
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] result = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputBytes, result);
        
        input.close();
    }
    
    @Test
    public void testCloseQuietly() {
        Closeable closeable = null;
        IOUtils.closeQuietly(closeable);
        
        closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(closeable);
    }

}