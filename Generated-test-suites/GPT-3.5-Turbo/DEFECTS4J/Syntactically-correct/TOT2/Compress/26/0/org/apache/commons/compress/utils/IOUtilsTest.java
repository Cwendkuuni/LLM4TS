import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.apache.commons.compress.utils.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream in = new ByteArrayInputStream("Test data".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(in, out);
        String result = out.toString("UTF-8");
        
        assertEquals("Test data", result);
        assertEquals(9, bytesCopied);
        
        in.close();
        out.close();
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream in = new ByteArrayInputStream("Another test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(in, out, 5);
        String result = out.toString("UTF-8");
        
        assertEquals("Another", result);
        assertEquals(7, bytesCopied);
        
        in.close();
        out.close();
    }

    @Test
    public void testSkip() throws IOException {
        InputStream in = new ByteArrayInputStream("Skip test data".getBytes());
        
        long bytesSkipped = IOUtils.skip(in, 5);
        
        assertEquals(5, bytesSkipped);
        
        in.close();
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[10];
        InputStream in = new ByteArrayInputStream("ReadFullyTest".getBytes());
        
        int bytesRead = IOUtils.readFully(in, buffer, 0, 10);
        
        String result = new String(buffer);
        
        assertEquals("ReadFullyT", result);
        assertEquals(10, bytesRead);
        
        in.close();
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] expected = "ToByteArrayTest".getBytes();
        InputStream in = new ByteArrayInputStream(expected);
        
        byte[] result = IOUtils.toByteArray(in);
        
        assertArrayEquals(expected, result);
        
        in.close();
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test close exception");
            }
        };
        
        IOUtils.closeQuietly(closeable);
    }
}