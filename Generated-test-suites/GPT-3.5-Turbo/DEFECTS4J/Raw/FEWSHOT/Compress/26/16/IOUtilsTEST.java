import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IOUtilsTest {
    @Test
    public void testCopy() throws IOException {
        // Test copying from input to output with default buffer size
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long result = IOUtils.copy(new ByteArrayInputStream(input), output);
        assertEquals(5, result);
        assertArrayEquals(input, output.toByteArray());
        
        // Test copying from input to output with custom buffer size
        byte[] input2 = {10, 20, 30, 40, 50};
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        long result2 = IOUtils.copy(new ByteArrayInputStream(input2), output2, 3);
        assertEquals(5, result2);
        assertArrayEquals(input2, output2.toByteArray());
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        InputStream inputstream = new ByteArrayInputStream(input);
        long result = IOUtils.skip(inputstream, 3);
        assertEquals(3, result);
        
        byte[] leftover = new byte[2];
        int read = inputstream.read(leftover);
        assertEquals(2, read);
        assertArrayEquals(new byte[]{4, 5}, leftover);
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        InputStream inputstream = new ByteArrayInputStream(input);
        byte[] buffer = new byte[5];
        
        int result = IOUtils.readFully(inputstream, buffer);
        assertEquals(5, result);
        assertArrayEquals(input, buffer);
        
        byte[] buffer2 = new byte[3];
        int result2 = IOUtils.readFully(inputstream, buffer2, 0, 3);
        assertEquals(2, result2);
        assertArrayEquals(new byte[]{4, 5, 0}, buffer2);
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputstream = new ByteArrayInputStream(input);
        byte[] result = IOUtils.toByteArray(inputstream);
        assertArrayEquals(input, result);
    }
    
    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        
        // Make sure no exception is thrown
        IOUtils.closeQuietly(closeable);
        
        // Make sure no exception is thrown when closeable is null
        IOUtils.closeQuietly(null);
    }
}