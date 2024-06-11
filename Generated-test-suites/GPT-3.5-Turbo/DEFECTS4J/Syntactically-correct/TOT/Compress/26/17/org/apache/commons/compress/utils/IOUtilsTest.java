
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {
    
    private static final int BUFFER_SIZE = 1024;

    @Test
    public void testCopy_InputStreamToOutputStream() throws IOException {
        // Test with a small buffer size and some content in the input stream
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 8);
        byte[] copiedBytes = output.toByteArray();
        
        assertEquals(13, count); // Verify the total number of bytes copied
        assertArrayEquals("Hello, World!".getBytes(), copiedBytes); // Verify the content of the copied bytes
        
        input.close();
        output.close();
    }

    @Test
    public void testCopy_InputStreamToOutputStream_EmptyInput() throws IOException {
        // Test with an empty input stream
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        byte[] copiedBytes = output.toByteArray();
        
        assertEquals(0, count); // Verify no bytes are copied since input stream is empty
        assertArrayEquals(new byte[0], copiedBytes); // Verify the content of the copied bytes is empty
        
        input.close();
        output.close();
    }

    @Test
    public void testCopy_InputStreamToOutputStream_MultipleBuffers() throws IOException {
        // Test with a large buffer size and multiple buffer read/write operations
        byte[] inputData = new byte[BUFFER_SIZE * 3];
        byte[] expectedData = new byte[BUFFER_SIZE * 3];
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = (byte) (i % 256);
            expectedData[i] = (byte) (i % 256);
        }
        
        InputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, BUFFER_SIZE * 2);
        byte[] copiedBytes = output.toByteArray();
        
        assertEquals(inputData.length, count); // Verify the total number of bytes copied
        assertArrayEquals(expectedData, copiedBytes); // Verify the content of the copied bytes
        
        input.close();
        output.close();
    }

    @Test
    public void testSkip_InputStream() throws IOException {
        // Test skipping a specific number of bytes in the input stream
        byte[] inputData = new byte[10];
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = (byte) (i % 256);
        }
        
        InputStream input = new ByteArrayInputStream(inputData);
        long skipped = IOUtils.skip(input, 5);
        long remaining = IOUtils.skip(input, 5);
        
        assertEquals(5, skipped); // Verify the total number of bytes skipped is correct
        assertEquals(0, remaining); // Verify no bytes are remaining to skip
        
        input.close();
    }

    @Test
    public void testReadFully_InputStream() throws IOException {
        // Test reading the full content of the input stream into a buffer
        byte[] inputData = new byte[BUFFER_SIZE];
        byte[] expectedData = new byte[BUFFER_SIZE];
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = (byte) (i % 256);
            expectedData[i] = (byte) (i % 256);
        }
        
        InputStream input = new ByteArrayInputStream(inputData);
        byte[] buffer = new byte[BUFFER_SIZE];
        int count = IOUtils.readFully(input, buffer);
        
        assertEquals(BUFFER_SIZE, count); // Verify the total number of bytes read
        assertArrayEquals(expectedData, buffer); // Verify the content of the read bytes
        
        input.close();
    }

    @Test
    public void testReadFully_InputStream_Offset() throws IOException {
        // Test reading a specific number of bytes from the input stream into a buffer with an offset
        byte[] inputData = new byte[BUFFER_SIZE];
        byte[] expectedData = new byte[BUFFER_SIZE];
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = (byte) (i % 256);
            expectedData[i] = (byte) (i % 256);
        }
        
        InputStream input = new ByteArrayInputStream(inputData);
        byte[] buffer = new byte[BUFFER_SIZE + 10];
        int count = IOUtils.readFully(input, buffer, 5, BUFFER_SIZE - 5);
        
        assertEquals(BUFFER_SIZE - 5, count); // Verify the total number of bytes read
        assertArrayEquals(expectedData, 0, buffer, 5, BUFFER_SIZE - 5); // Verify the content of the read bytes
        
        input.close();
    }

    @Test
    public void testToByteArray_InputStream() throws IOException {
        // Test converting an input stream to a byte array
        byte[] inputData = new byte[BUFFER_SIZE];
        byte[] expectedData = new byte[BUFFER_SIZE];
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = (byte) (i % 256);
            expectedData[i] = (byte) (i % 256);
        }
        
        InputStream input = new ByteArrayInputStream(inputData);
        byte[] result = IOUtils.toByteArray(input);
        
        assertArrayEquals(expectedData, result); // Verify the content of the byte array
        
        input.close();
    }

    @Test
    public void testCloseQuietly() throws IOException {
        // Test closing a Closeable object, including a null argument
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                // Do nothing                
            }
        };
        
        IOUtils.closeQuietly(closeable);
        
        closeable = null;
        IOUtils.closeQuietly(closeable); // Verify that it doesn't throw any exception
    }
}

