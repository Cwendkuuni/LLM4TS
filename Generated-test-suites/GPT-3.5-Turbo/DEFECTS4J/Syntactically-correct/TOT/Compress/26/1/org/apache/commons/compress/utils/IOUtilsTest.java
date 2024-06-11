
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class IOUtilsTest {
    
    // Test case for copy method with default buffer size
    @Test
    public void testCopyWithDefaultBufferSize() {
        try {
            InputStream input = new ByteArrayInputStream("Test String".getBytes());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            long result = IOUtils.copy(input, output);
            String outputString = output.toString();
            
            // Check if the copied content is correct
            assertEquals("Test String", outputString);
            
            // Check if the returned count is correct
            assertEquals(11, result);
            
            // Check if the input stream is closed
            assertTrue(input.read() == -1);
            
            input.close();
            output.close();
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for copy method with custom buffer size
    @Test
    public void testCopyWithCustomBufferSize() {
        try {
            InputStream input = new ByteArrayInputStream("Test String".getBytes());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int bufferSize = 10;
            
            long result = IOUtils.copy(input, output, bufferSize);
            
            String outputString = output.toString();
            
            // Check if the copied content is correct
            assertEquals("Test String", outputString);
            
            // Check if the returned count is correct
            assertEquals(11, result);
            
            // Check if the input stream is closed
            assertTrue(input.read() == -1);
            
            input.close();
            output.close();
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for skip method
    @Test
    public void testSkip() {
        try {
            InputStream input = new ByteArrayInputStream("Test String".getBytes());
            long numToSkip = 5;
            
            long result = IOUtils.skip(input, numToSkip);
            
            // Check if the correct number of bytes is skipped
            assertEquals(numToSkip, result);
            
            // Check if the remaining content of input stream is correct
            StringBuilder remainingContent = new StringBuilder();
            int character;
            while ((character = input.read()) != -1) {
                remainingContent.append((char) character);
            }
            assertEquals("String", remainingContent.toString());
            
            input.close();
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for readFully method with full buffer
    @Test
    public void testReadFullyFullBuffer() {
        try {
            InputStream input = new ByteArrayInputStream("Test String".getBytes());
            byte[] buffer = new byte[11];
            
            int result = IOUtils.readFully(input, buffer);
            
            // Check if the correct number of bytes is read
            assertEquals(11, result);
            
            // Check if the content of buffer is correct
            String bufferContent = new String(buffer, 0, result);
            assertEquals("Test String", bufferContent);
            
            input.close();
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for readFully method with partial buffer
    @Test
    public void testReadFullyPartialBuffer() {
        try {
            InputStream input = new ByteArrayInputStream("Test String".getBytes());
            byte[] buffer = new byte[5];
            
            int result = IOUtils.readFully(input, buffer);
            
            // Check if the correct number of bytes is read
            assertEquals(5, result);
            
            // Check if the content of buffer is correct
            String bufferContent = new String(buffer, 0, result);
            assertEquals("Test ", bufferContent);
            
            input.close();
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for toByteArray method
    @Test
    public void testToByteArray() {
        try {
            InputStream input = new ByteArrayInputStream("Test String".getBytes());
            
            byte[] result = IOUtils.toByteArray(input);
            
            // Check if the correct byte array is returned
            assertArrayEquals("Test String".getBytes(), result);
            
            input.close();
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for closeQuietly method with non-null Closeable
    @Test
    public void testCloseQuietlyNotNullCloseable() {
        try {
            Closeable c = new ByteArrayInputStream("Test String".getBytes());
            
            IOUtils.closeQuietly(c);
            
            // Check if the Closeable is closed successfully
            assertFalse(c instanceof ByteArrayInputStream);
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for closeQuietly method with null Closeable
    @Test
    public void testCloseQuietlyNullCloseable() {
        Closeable c = null;
        
        // Check if the method does not throw any exception
        IOUtils.closeQuietly(c);
    }
}

