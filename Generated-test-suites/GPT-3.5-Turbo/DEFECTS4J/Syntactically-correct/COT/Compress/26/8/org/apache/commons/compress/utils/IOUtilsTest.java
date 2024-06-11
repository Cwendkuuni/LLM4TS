import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        // Test case 1: Normal case
        InputStream input = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
        long expected = 0;
        long result = IOUtils.copy(input, output);
        assertEquals(expected, result);
        
        // Test case 2: Testing buffer size
        input = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
        int bufferSize = 512;
        expected = 0;
        result = IOUtils.copy(input, output, bufferSize);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSkip() throws Exception {
        // Test case 1: Normal case
        InputStream input = new InputStream() {
            @Override
            public long skip(long n) throws IOException {
                return n;
            }
        };
        long numToSkip = 10;
        long expected = numToSkip;
        long result = IOUtils.skip(input, numToSkip);
        assertEquals(expected, result);
        
        // Test case 2: Edge case - numToSkip is less than the number of skipped bytes
        input = new InputStream() {
            @Override
            public long skip(long n) throws IOException {
                return n - 1;
            }
        };
        numToSkip = 10;
        expected = numToSkip - 1;
        result = IOUtils.skip(input, numToSkip);
        assertEquals(expected, result);
    }
    
    @Test
    public void testReadFully() throws Exception {
        // Test case 1: Normal case
        InputStream input = new InputStream() {
            @Override
            public int read(byte[] b, int offset, int len) throws IOException {
                return len;
            }
        };
        byte[] buffer = new byte[10];
        int offset = 0;
        int len = 10;
        int expected = len;
        int result = IOUtils.readFully(input, buffer, offset, len);
        assertEquals(expected, result);
        
        // Test case 2: Edge cases for parameters
        input = new InputStream() {
            @Override
            public int read(byte[] b, int offset, int len) throws IOException {
                return len - 1;
            }
        };
        buffer = new byte[10];
        offset = 5;
        len = 6;
        expected = len - 1;
        result = IOUtils.readFully(input, buffer, offset, len);
        assertEquals(expected, result);
    }
    
    @Test
    public void testToByteArray() throws Exception {
        // Test case 1: Normal case
        InputStream input = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        byte[] expected = new byte[0];
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(expected, result);
        
        // Test case 2: Edge case - inputStream is null
        input = null;
        expected = null;
        result = IOUtils.toByteArray(input);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testCloseQuietly() throws Exception {
        // Test case 1: Normal case - Closeable is not null
        Closeable c = new Closeable() {
            @Override
            public void close() throws IOException {
            }
        };
        IOUtils.closeQuietly(c);
        
        // Test case 2: Edge case - Closeable is null
        c = null;
        IOUtils.closeQuietly(c);
    }
}