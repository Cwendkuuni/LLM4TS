import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        // Typical use case
        byte[] inputData = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output);
        
        assertEquals(inputData.length, bytesCopied);
        assertArrayEquals(inputData, output.toByteArray());
        
        // Edge case
        input = new ByteArrayInputStream(new byte[0]);
        output = new ByteArrayOutputStream();
        
        bytesCopied = IOUtils.copy(input, output);
        
        assertEquals(0, bytesCopied);
    }
    
    @Test
    public void testSkip() throws IOException {
        // Typical use case
        byte[] data = "Skipping text".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        
        long bytesSkipped = IOUtils.skip(input, 8);
        
        assertEquals(8, bytesSkipped);
        
        // Edge case
        input = new ByteArrayInputStream(data);
        
        bytesSkipped = IOUtils.skip(input, 20);
        
        assertEquals(data.length, bytesSkipped);
    }
    
    @Test
    public void testReadFully() throws IOException {
        // Typical use case
        byte[] data = "ReadFully".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        byte[] output = new byte[data.length];
        
        int bytesRead = IOUtils.readFully(input, output);
        
        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, output);
        
        // Edge case
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(data);
        outputStream.write("ExtraData".getBytes());
        input = new ByteArrayInputStream(outputStream.toByteArray());
        output = new byte[data.length];
        
        bytesRead = IOUtils.readFully(input, output, 0, data.length);
        
        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, output);
        
        // Error scenario
        input = new ByteArrayInputStream(data);
        output = new byte[10]; 
        
        try {
            bytesRead = IOUtils.readFully(input, output, 0, 20);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testToByteArray() throws IOException {
        // Typical use case
        byte[] inputData = "ByteArray".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        
        byte[] outputData = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputData, outputData);
    }
    
    @Test
    public void testCloseQuietly() {
        // Typical use case
        CloseableMock closeable = new CloseableMock();
        
        IOUtils.closeQuietly(closeable);
        
        assertTrue(closeable.closed);
        
        // Error scenario
        IOUtils.closeQuietly(null); // Should not throw any exceptions
    }
    
    // Helper class to mock Closeable
    private class CloseableMock implements Closeable {
        public boolean closed = false;
        
        @Override
        public void close() {
            closed = true;
        }
    }
}