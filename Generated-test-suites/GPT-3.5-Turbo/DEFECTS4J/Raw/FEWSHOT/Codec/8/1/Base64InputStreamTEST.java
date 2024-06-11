import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class Base64InputStreamTest {
    private static final byte[] TEST_DATA = "Hello, World!".getBytes();
    
    @Test
    public void testRead() throws IOException {
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(TEST_DATA));
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        int read = base64In.read();
        int index = 0;
        while (read != -1) {
            assertEquals(TEST_DATA[index++], (byte) read);
            read = base64In.read();
        }
        
        base64In.close();
        in.close();
    }
    
    @Test
    public void testRead_withOffsetAndLength() throws IOException {
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(TEST_DATA));
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        byte[] buffer = new byte[10];
        int read = base64In.read(buffer, 2, 6);
        int index = 0;
        while (read != -1) {
            for (int i = 0; i < read; i++) {
                assertEquals(TEST_DATA[index++], buffer[i + 2]);
            }
            read = base64In.read(buffer, 2, 6);
        }
        
        base64In.close();
        in.close();
    }
    
    @Test
    public void testRead_withEmptyInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        int read = base64In.read();
        assertEquals(-1, read);
        
        base64In.close();
        in.close();
    }
    
    @Test
    public void testRead_withZeroLengthBuffer() throws IOException {
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(TEST_DATA));
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        byte[] buffer = new byte[0];
        int read = base64In.read(buffer, 0, 0);
        assertEquals(0, read);
        
        base64In.close();
        in.close();
    }
    
    @Test
    public void testRead_withNullBuffer() throws IOException {
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(TEST_DATA));
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        byte[] buffer = null;
        try {
            base64In.read(buffer, 0, 1);
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
        
        base64In.close();
        in.close();
    }
    
    @Test
    public void testRead_withInvalidOffset() throws IOException {
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(TEST_DATA));
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        byte[] buffer = new byte[10];
        try {
            base64In.read(buffer, -1, 6);
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
        
        base64In.close();
        in.close();
    }
    
    @Test
    public void testRead_withInvalidLength() throws IOException {
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(TEST_DATA));
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        byte[] buffer = new byte[10];
        try {
            base64In.read(buffer, 2, -1);
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
        
        base64In.close();
        in.close();
    }
    
    @Test
    public void testRead_withInvalidBufferLength() throws IOException {
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(TEST_DATA));
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        byte[] buffer = new byte[5];
        try {
            base64In.read(buffer, 0, 6);
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
        
        base64In.close();
        in.close();
    }
    
    @Test
    public void testClose() throws IOException {
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(TEST_DATA));
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        base64In.close();
        assertTrue(base64In.isClosed());
        assertTrue(in.read() == -1);
    }
    
    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(TEST_DATA));
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        assertFalse(base64In.markSupported());
        
        base64In.close();
    }
}