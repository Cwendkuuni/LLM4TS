import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {
    
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(13, count);
        assertEquals("Hello, World!", output.toString());
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 10);
        assertEquals(13, count);
        assertEquals("Hello, World!", output.toString());
    }
    
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        long skipped = IOUtils.skip(input, 7);
        assertEquals(7, skipped);
    }
    
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(5, bytesRead);
        assertEquals("Hello", new String(buffer));
    }
    
    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[10];
        int bytesRead = IOUtils.readFully(input, buffer, 2, 7);
        assertEquals(7, bytesRead);
        assertEquals("\u0000\u0000Hello, W", new String(buffer));
    }
    
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] result = IOUtils.toByteArray(input);
        assertEquals("Hello, World!", new String(result));
    }
    
    @Test
    public void testCloseQuietly() {
        // Test with a non-null Closeable
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("test exception");
            }
        };
        IOUtils.closeQuietly(closeable);
        
        // Test with a null Closeable
        Closeable nullCloseable = null;
        IOUtils.closeQuietly(nullCloseable);
        
        // Assuming no exception is thrown, the test is considered passed
    }
}