import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class IOUtilsTest {

    // Test for method copy(final InputStream input, final OutputStream output)
    @Test
    public void testCopy() throws IOException {
        // Prepare input stream with test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        // Create mock output stream
        OutputStream outputStream = mock(OutputStream.class);

        // Call the method under test
        long result = IOUtils.copy(inputStream, outputStream);

        // Verify that the method returns the correct value
        assertEquals(17, result);

        // Verify that the output stream received the correct bytes
        verify(outputStream).write("Test Input Stream".getBytes());

        // Verify that the output stream gets closed
        verify(outputStream).close();
    }
    
    // Test for method copy(final InputStream input, final OutputStream output, int buffersize)
    @Test
    public void testCopyWithBuffer() throws IOException {
        // Prepare input stream with test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        // Create mock output stream
        OutputStream outputStream = mock(OutputStream.class);

        // Call the method under test
        long result = IOUtils.copy(inputStream, outputStream, 10);

        // Verify that the method returns the correct value
        assertEquals(17, result);

        // Verify that the output stream received the correct bytes
        verify(outputStream).write("Test Input Stream".getBytes());

        // Verify that the output stream gets closed
        verify(outputStream).close();
    }
    
    // Test for method skip(InputStream input, long numToSkip)
    @Test
    public void testSkip() throws IOException {
        // Prepare input stream with test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        
        // Call the method under test
        long result = IOUtils.skip(inputStream, 5);

        // Verify that the method returns the correct value
        assertEquals(5, result);

        // Verify that the input stream skips the correct number of bytes
        assertEquals(5, inputStream.skip(5));

        // Verify that the input stream gets closed
        inputStream.close();
    }
    
    // Test for method readFully(InputStream input, byte[] b)
    @Test
    public void testReadFully() throws IOException {
        // Prepare input stream with test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        byte[] buffer = new byte[10];
        
        // Call the method under test
        int result = IOUtils.readFully(inputStream, buffer);

        // Verify that the method returns the correct value
        assertEquals(10, result);

        // Verify that the input stream reads the correct number of bytes
        inputStream.mark(0);
        byte[] readBytes = new byte[10];
        inputStream.read(readBytes);
        assertEquals("Test Input", new String(readBytes));

        // Verify that the input stream gets closed
        inputStream.close();
    }
    
    // Test for method readFully(InputStream input, byte[] b, int offset, int len)
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        // Prepare input stream with test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        byte[] buffer = new byte[10];
        
        // Call the method under test
        int result = IOUtils.readFully(inputStream, buffer, 2, 6);

        // Verify that the method returns the correct value
        assertEquals(6, result);

        // Verify that the input stream reads the correct number of bytes
        inputStream.mark(0);
        byte[] readBytes = new byte[6];
        inputStream.read(readBytes);
        assertEquals("st Inp", new String(readBytes));

        // Verify that the input stream gets closed
        inputStream.close();
    }
    
    // Test for method toByteArray(final InputStream input)
    @Test
    public void testToByteArray() throws IOException {
        // Prepare input stream with test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        
        // Call the method under test
        byte[] result = IOUtils.toByteArray(inputStream);

        // Verify that the method returns the correct value
        assertEquals("Test Input Stream", new String(result));

        // Verify that the input stream gets closed
        inputStream.close();
    }
    
    // Test for method closeQuietly(Closeable c)
    @Test
    public void testCloseQuietly() throws IOException {
        // Create mock closeable
        Closeable closeable = mock(Closeable.class);

        // Call the method under test
        IOUtils.closeQuietly(closeable);

        // Verify that the closeable gets closed
        verify(closeable).close();
    }

}