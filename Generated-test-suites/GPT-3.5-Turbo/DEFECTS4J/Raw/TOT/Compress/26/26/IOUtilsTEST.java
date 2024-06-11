
import org.junit.*;
import java.io.*;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy_defaultBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        assertEquals("Hello, World!".getBytes().length, count);
        assertEquals("Hello, World!", output.toString());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testCopy_customBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output, 10);
        
        assertEquals("Hello, World!".getBytes().length, count);
        assertEquals("Hello, World!", output.toString());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testCopy_emptyInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        assertEquals(0, count);
        assertEquals("", output.toString());
        
        input.close();
        output.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testCopy_nullInputStream() throws IOException {
        InputStream input = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        IOUtils.copy(input, output);

        output.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testCopy_nullOutputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream output = null;
        
        IOUtils.copy(input, output);
        
        input.close();
    }

    @Test
    public void testSkip_moreBytesToSkipThanAvailable() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        
        long skipped = IOUtils.skip(input, 100);
        
        assertEquals("Hello, World!".getBytes().length, skipped);
        
        input.close();
    }
    
    @Test
    public void testSkip_lessBytesToSkipThanAvailable() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        
        long skipped = IOUtils.skip(input, 5);
        
        assertEquals(5, skipped);
        
        input.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testSkip_nullInputStream() throws IOException {
        InputStream input = null;
        
        IOUtils.skip(input, 5);
    }
    
    @Test
    public void testSkip_zeroBytesToSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        
        long skipped = IOUtils.skip(input, 0);
        
        assertEquals(0, skipped);
        
        input.close();
    }
    
    @Test
    public void testReadFully_readAllBytes() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] output = new byte[14];
        
        int bytesRead = IOUtils.readFully(input, output);
        
        assertEquals("Hello, World!".getBytes().length, bytesRead);
        assertArrayEquals("Hello, World!".getBytes(), output);
        
        input.close();
    }
    
    @Test
    public void testReadFully_readPartialBytes() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] output = new byte[5];
        
        int bytesRead = IOUtils.readFully(input, output);
        
        assertEquals(5, bytesRead);
        assertArrayEquals("Hello".getBytes(), output);
        
        input.close();
    }
    
    @Test
    public void testReadFully_zeroBytesToRead() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] output = new byte[0];
        
        int bytesRead = IOUtils.readFully(input, output);
        
        assertEquals(0, bytesRead);
        
        input.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadFully_nullInputStream() throws IOException {
        InputStream input = null;
        byte[] output = new byte[10];
        
        IOUtils.readFully(input, output);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFully_invalidOffset() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] output = new byte[10];
        
        IOUtils.readFully(input, output, -1, 5);
        
        input.close();
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFully_invalidLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] output = new byte[10];
        
        IOUtils.readFully(input, output, 0, 20);
        
        input.close();
    }
    
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());

        byte[] byteArray = IOUtils.toByteArray(input);

        assertArrayEquals("Hello, World!".getBytes(), byteArray);

        input.close();
    }
    
    @Test
    public void testToByteArray_emptyInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);

        byte[] byteArray = IOUtils.toByteArray(input);

        assertEquals(0, byteArray.length);

        input.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testToByteArray_nullInputStream() throws IOException {
        InputStream input = null;

        IOUtils.toByteArray(input);
    }

    @Test
    public void testCloseQuietly_closeableNotNull() {
        Closeable closeable = new ByteArrayInputStream("Hello, World!".getBytes());
        
        IOUtils.closeQuietly(closeable);
        
        // No assertion required, just making sure it doesn't throw an exception
        
        closeable = new ByteArrayOutputStream();
        
        IOUtils.closeQuietly(closeable);
        
        // No assertion required, just making sure it doesn't throw an exception
        
        closeable = new FilterOutputStream(new ByteArrayOutputStream());
        
        IOUtils.closeQuietly(closeable);
        
        // No assertion required, just making sure it doesn't throw an exception
        
        closeable = new PipedOutputStream();
        
        IOUtils.closeQuietly(closeable);
        
        // No assertion required, just making sure it doesn't throw an exception
    }
    
    @Test
    public void testCloseQuietly_closeableNull() {
        Closeable closeable = null;
        
        IOUtils.closeQuietly(closeable);
        
        // No assertion required, just making sure it doesn't throw an exception
    }
}

