
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class IOUtilsTest {
    @Test
    public void testCopy() {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        try {
            long count = IOUtils.copy(input, output);
            assertEquals(11, count);
            assertEquals("test string", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testCopyWithBufferSize() {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        try {
            long count = IOUtils.copy(input, output, 5);
            assertEquals(11, count);
            assertEquals("test string", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testSkip() {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        
        try {
            long skipped = IOUtils.skip(input, 5);
            assertEquals(5, skipped);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testReadFully() {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        byte[] buffer = new byte[5];
        
        try {
            int bytesRead = IOUtils.readFully(input, buffer);
            assertEquals(5, bytesRead);
            assertEquals("test ", new String(buffer));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        byte[] buffer = new byte[5];
        
        try {
            int bytesRead = IOUtils.readFully(input, buffer, 1, 4);
            assertEquals(4, bytesRead);
            assertEquals("est ", new String(buffer, 1, 4));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testToByteArray() {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        
        try {
            byte[] result = IOUtils.toByteArray(input);
            assertArrayEquals("test string".getBytes(), result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testCloseQuietly() {
        class MockCloseable implements Closeable {
            public boolean closed = false;
            
            public void close() throws IOException {
                closed = true;
            }
        }
        
        MockCloseable closeable = new MockCloseable();
        IOUtils.closeQuietly(closeable);
        assertTrue(closeable.closed);
    }
}
