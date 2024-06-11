import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {
    
    @Test
    public void testGetNextArEntry() throws IOException {
        InputStream input = null;
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        
        // Test when offset is 0
        arInputStream.offset = 0;
        arInputStream.read();
        
        try {
            ArArchiveEntry entry = arInputStream.getNextArEntry();
            assertNotNull(entry);
            assertEquals("TestEntry", entry.getName());
            assertEquals(10, entry.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test when input is null
        arInputStream.input = null;
        try {
            ArArchiveEntry entry = arInputStream.getNextArEntry();
            assertNull(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        InputStream input = null;
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        
        // Test when offset is 0
        arInputStream.offset = 0;
        arInputStream.read();
        
        try {
            ArchiveEntry entry = arInputStream.getNextEntry();
            assertNotNull(entry);
            assertEquals("TestEntry", entry.getName());
            assertEquals(10, entry.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test when input is null
        arInputStream.input = null;
        try {
            ArchiveEntry entry = arInputStream.getNextEntry();
            assertNull(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testClose() throws IOException {
        InputStream input = null;
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        
        // Test when closed is false
        arInputStream.closed = false;
        try {
            arInputStream.close();
            assertTrue(arInputStream.closed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Test when closed is true
        arInputStream.closed = true;
        try {
            arInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testRead() throws IOException {
        InputStream input = null;
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        
        // Test when ret > 0
        input = new InputStream() {
            @Override
            public int read() throws IOException {
                // Implement mock read method
                return 1;
            }
        };
        arInputStream.input = input;
        int ret = arInputStream.read();
        assertEquals(1, ret);
        assertEquals(1, arInputStream.offset);
        
        // Test when ret <= 0
        input = new InputStream() {
            @Override
            public int read() throws IOException {
                // Implement mock read method
                return -1;
            }
        };
        arInputStream.input = input;
        ret = arInputStream.read();
        assertEquals(-1, ret);
        assertEquals(1, arInputStream.offset);
    }
    
    @Test
    public void testRead_byteArray() throws IOException {
        InputStream input = null;
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        
        // Test when ret > 0
        final int len = 3;
        final byte[] b = new byte[len];
        input = new InputStream() {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                // Implement mock read method
                b[0] = 1;
                b[1] = 2;
                b[2] = 3;
                return len;
            }
        };
        arInputStream.input = input;
        int ret = arInputStream.read(b);
        assertEquals(len, ret);
        assertEquals(len, arInputStream.offset);
        assertEquals(1, b[0]);
        assertEquals(2, b[1]);
        assertEquals(3, b[2]);
        
        // Test when ret <= 0
        input = new InputStream() {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                // Implement mock read method
                return -1;
            }
        };
        arInputStream.input = input;
        ret = arInputStream.read(b);
        assertEquals(-1, ret);
        assertEquals(len, arInputStream.offset);
    }
    
    @Test
    public void testRead_byteArray_off_len() throws IOException {
        InputStream input = null;
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        
        // Test when ret > 0
        final int len = 3;
        final int off = 1;
        final byte[] b = new byte[len + off];
        input = new InputStream() {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                // Implement mock read method
                b[1] = 1;
                b[2] = 2;
                b[3] = 3;
                return len;
            }
        };
        arInputStream.input = input;
        int ret = arInputStream.read(b, off, len);
        assertEquals(len, ret);
        assertEquals(len, arInputStream.offset);
        assertEquals(0, b[0]);
        assertEquals(1, b[1]);
        assertEquals(2, b[2]);
        assertEquals(3, b[3]);
        
        // Test when ret <= 0
        input = new InputStream() {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                // Implement mock read method
                return -1;
            }
        };
        arInputStream.input = input;
        ret = arInputStream.read(b, off, len);
        assertEquals(-1, ret);
        assertEquals(len, arInputStream.offset);
    }
    
    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        
        // Test when length < 8
        int length = 7;
        boolean matches = ArArchiveInputStream.matches(signature, length);
        assertFalse(matches);
        
        // Test when signature does not match
        length = 8;
        signature = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        matches = ArArchiveInputStream.matches(signature, length);
        assertFalse(matches);
        
        // Test when signature matches
        length = 8;
        signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        matches = ArArchiveInputStream.matches(signature, length);
        assertTrue(matches);
    }
}