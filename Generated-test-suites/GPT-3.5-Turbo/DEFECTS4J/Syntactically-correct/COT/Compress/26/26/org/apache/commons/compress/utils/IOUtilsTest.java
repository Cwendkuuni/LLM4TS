
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.apache.commons.compress.utils.IOUtils;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        // Initialize test input and output streams
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();

        // Call the method under test
        long count = IOUtils.copy(input, output);

        // Assert the output is correct
        byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals("Copied bytes are incorrect", inputBytes, outputBytes);
        assertEquals("Count of copied bytes is incorrect", inputBytes.length, count);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        // Initialize test input and output streams
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();
        int bufferSize = 2;

        // Call the method under test
        long count = IOUtils.copy(input, output, bufferSize);

        // Assert the output is correct
        byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals("Copied bytes are incorrect", inputBytes, outputBytes);
        assertEquals("Count of copied bytes is incorrect", inputBytes.length, count);
    }
  
    @Test
    public void testSkip() throws IOException{
        // Initialize test input stream and number of bytes to skip
        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(bytes);
        long numToSkip = 3;
        
        // Call the method under test
        long skipped = IOUtils.skip(input, numToSkip);
        
        // Assert the number of bytes skipped is correct
        assertEquals("Number of skipped bytes is incorrect", numToSkip, skipped);
    }
    
    @Test
    public void testReadFully() throws IOException{
        // Initialize test input stream and buffer
        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(bytes);
        byte[] buffer = new byte[5];
        
        // Call the method under test
        int count = IOUtils.readFully(input, buffer);
        
        // Assert the number of bytes read and the content of the buffer are correct
        assertEquals("Number of read bytes is incorrect", bytes.length, count);
        assertArrayEquals("Read bytes are incorrect", bytes, buffer);
    }
  
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException{
        // Initialize test input stream, buffer, offset, and length
        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(bytes);
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        
        // Call the method under test
        int count = IOUtils.readFully(input, buffer, offset, length);
        
        // Assert the number of bytes read and the content of the buffer are correct
        assertEquals("Number of read bytes is incorrect", bytes.length, count);
        assertArrayEquals("Read bytes are incorrect", bytes, buffer);
    }
  
    @Test
    public void testToByteArray() throws IOException {
        // Initialize test input stream
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);

        // Call the method under test
        byte[] outputBytes = IOUtils.toByteArray(input);

        // Assert the output is correct
        assertArrayEquals("Copied bytes are incorrect", inputBytes, outputBytes);
    }
  
    @Test
    public void testCloseQuietly() throws IOException{
        // Initialize test closeable
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                // Simulate closing by throwing IOException
                throw new IOException();
            }
        };
        
        // Call the method under test and assert no exception is thrown
        IOUtils.closeQuietly(closeable);
    }
}

