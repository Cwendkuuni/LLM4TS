import static org.junit.Assert.*;
import org.junit.Test;

public class IOUtilsTest {
    
    @Test
    public void testCopy() {
        // Create an input stream and output stream
        // that will be used for testing the copy method
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        try {
            // Call the copy method and store the return value
            long result = IOUtils.copy(input, output);
            
            // Check that the copied bytes match the input
            assertEquals("Hello World!", new String(output.toByteArray()));
            
            // Check that the return value matches the number of copied bytes
            assertEquals(12, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testCopyWithBufferSize() {
        // Create an input stream and output stream
        // that will be used for testing the copy method
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        try {
            // Call the copy method with a buffer size of 5
            long result = IOUtils.copy(input, output, 5);
            
            // Check that the copied bytes match the input
            assertEquals("Hello World!", new String(output.toByteArray()));
            
            // Check that the return value matches the number of copied bytes
            assertEquals(12, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testSkip() {
        // Create an input stream that will be used for testing the skip method
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        
        try {
            // Call the skip method and store the return value
            long result = IOUtils.skip(input, 6);
            
            // Check that the skipped bytes match the expected value
            assertEquals(6, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testReadFully() {
        // Create an input stream that will be used for testing the readFully method
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] buffer = new byte[5];
        
        try {
            // Call the readFully method and store the return value
            int result = IOUtils.readFully(input, buffer);
            
            // Check that the buffer is filled with the expected bytes
            assertArrayEquals("Hello".getBytes(), buffer);
            
            // Check that the return value matches the number of read bytes
            assertEquals(5, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() {
        // Create an input stream that will be used for testing the readFully method
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] buffer = new byte[5];
        
        try {
            // Call the readFully method with an offset of 2 and a length of 3
            int result = IOUtils.readFully(input, buffer, 2, 3);
            
            // Check that the buffer is filled with the expected bytes
            byte[] expected = new byte[] { 0, 0, 'H', 'e', 'l' };
            assertArrayEquals(expected, buffer);
            
            // Check that the return value matches the number of read bytes
            assertEquals(3, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testToByteArray() {
        // Create an input stream that will be used for testing the toByteArray method
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        
        try {
            // Call the toByteArray method and store the return value
            byte[] result = IOUtils.toByteArray(input);
            
            // Check that the result matches the expected byte array
            assertArrayEquals("Hello World!".getBytes(), result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testCloseQuietly() {
        // Create a closeable object that will be used for testing the closeQuietly method
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
        
        // Call the closeQuietly method with the closeable object
        IOUtils.closeQuietly(closeable);
        
        // No assertion is made as the method does not return a value
    }
}