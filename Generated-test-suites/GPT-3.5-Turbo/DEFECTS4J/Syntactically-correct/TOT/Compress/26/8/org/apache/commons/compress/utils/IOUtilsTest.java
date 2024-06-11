import org.junit.Test;
import org.junit.Assert;

public class IOUtilsTest {

    private static final int COPY_BUF_SIZE = 8024;
    private static final int SKIP_BUF_SIZE = 4096;
    
    @Test
    public void testCopyInputStreamToOutputStream() {
        InputStream input = null;
        OutputStream output = null;
        try {
            // Create a test input stream and output stream
            input = new ByteArrayInputStream("Test input stream".getBytes());
            output = new ByteArrayOutputStream();
            
            // Call the copy method with default buffer size
            long count = IOUtils.copy(input, output);
            
            // Verify that the output stream contains the same content as the input stream
            Assert.assertEquals("Test input stream", output.toString());
            
            // Verify that the correct number of bytes were copied
            Assert.assertEquals(17, count);
        } catch (IOException e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        } finally {
            // Close the input and output streams
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }
    
    @Test
    public void testCopyInputStreamToOutputStreamWithBufferSize() {
        InputStream input = null;
        OutputStream output = null;
        try {
            // Create a test input stream and output stream
            input = new ByteArrayInputStream("Test input stream".getBytes());
            output = new ByteArrayOutputStream();
            
            // Call the copy method with a custom buffer size
            long count = IOUtils.copy(input, output, 10);
            
            // Verify that the output stream contains the same content as the input stream
            Assert.assertEquals("Test input stream", output.toString());
            
            // Verify that the correct number of bytes were copied
            Assert.assertEquals(17, count);
        } catch (IOException e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        } finally {
            // Close the input and output streams
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }
    
    @Test
    public void testSkipBytesInInputStream() {
        InputStream input = null;
        try {
            // Create a test input stream
            input = new ByteArrayInputStream("Test input stream".getBytes());
            
            // Call the skip method to skip the first 5 bytes
            long skipped = IOUtils.skip(input, 5);
            
            // Verify that the correct number of bytes were skipped
            Assert.assertEquals(5, skipped);
            
            // Read the remaining content of the input stream
            byte[] buffer = new byte[17];
            int count = IOUtils.readFully(input, buffer);
            
            // Verify that the correct number of bytes were read
            Assert.assertEquals(12, count);
            
            // Verify that the remaining content of the input stream matches the expected value
            Assert.assertEquals("input stream", new String(buffer));
        } catch (IOException e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        } finally {
            // Close the input stream
            IOUtils.closeQuietly(input);
        }
    }
    
    @Test
    public void testReadFullyFromInputStream() {
        InputStream input = null;
        try {
            // Create a test input stream
            input = new ByteArrayInputStream("Test input stream".getBytes());
            
            // Read the entire content of the input stream into a byte array
            byte[] buffer = new byte[17];
            int count = IOUtils.readFully(input, buffer);
            
            // Verify that the correct number of bytes were read
            Assert.assertEquals(17, count);
            
            // Verify that the content of the byte array matches the expected value
            Assert.assertEquals("Test input stream", new String(buffer));
        } catch (IOException e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        } finally {
            // Close the input stream
            IOUtils.closeQuietly(input);
        }
    }
    
    @Test
    public void testReadFullyFromInputStreamWithOffsetAndLength() {
        InputStream input = null;
        try {
            // Create a test input stream
            input = new ByteArrayInputStream("Test input stream".getBytes());
            
            // Read a portion of the content of the input stream into a byte array
            byte[] buffer = new byte[5];
            int count = IOUtils.readFully(input, buffer, 1, 3);
            
            // Verify that the correct number of bytes were read
            Assert.assertEquals(3, count);
            
            // Verify that the content of the byte array matches the expected value
            Assert.assertEquals("est", new String(buffer));
        } catch (IOException e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        } finally {
            // Close the input stream
            IOUtils.closeQuietly(input);
        }
    }
    
    @Test
    public void testToByteArrayFromInputStream() {
        InputStream input = null;
        try {
            // Create a test input stream
            input = new ByteArrayInputStream("Test input stream".getBytes());
            
            // Convert the entire content of the input stream to a byte array
            byte[] result = IOUtils.toByteArray(input);
            
            // Verify that the byte array matches the expected value
            Assert.assertEquals("Test input stream", new String(result));
        } catch (IOException e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        } finally {
            // Close the input stream
            IOUtils.closeQuietly(input);
        }
    }
    
    @Test
    public void testCloseQuietly() {
        // Create a test closeable object
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Close failed");
            }
        };
        
        // Call the closeQuietly method and verify that no exception is thrown
        try {
            IOUtils.closeQuietly(closeable);
        } catch (Exception e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }
}