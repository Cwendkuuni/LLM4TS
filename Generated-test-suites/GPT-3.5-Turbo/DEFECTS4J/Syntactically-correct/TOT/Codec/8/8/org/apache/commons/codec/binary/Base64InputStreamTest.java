import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class Base64InputStreamTest {
    
    private static Base64InputStream base64InputStream;
    
    private static class MockInputStream extends ByteArrayInputStream {
        
        public MockInputStream(byte[] buf) {
            super(buf);
        }
        
        @Override
        public int read() {
            // Always return an arbitrary byte for testing
            return 0xFF;
        }
        
        @Override
        public int read(byte[] b, int off, int len) {
            // Always return an arbitrary byte array for testing
            return 3;
        }
    }
    
    @Before
    public void setUp() {
        InputStream inputStream = new MockInputStream(new byte[] {0x00, 0x01, 0x02, 0x03});
        base64InputStream = new Base64InputStream(inputStream);
    }
    
    @Test
    public void testRead_shouldReturnDecodedBytes() throws IOException {
        byte[] expected = new byte[]{0x00, 0x01, 0x02, 0x03};
        byte[] actual = new byte[4];
        int len = base64InputStream.read(actual);
        assertEquals(4, len);
        assertArrayEquals(expected, actual);
    }
    
    @Test(expected = NullPointerException.class)
    public void testRead_withNullArray_shouldThrowNullPointerException() throws IOException {
        base64InputStream.read(null, 0, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withNegativeOffset_shouldThrowIndexOutOfBoundsException() throws IOException {
        byte[] b = new byte[4];
        base64InputStream.read(b, -1, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidLength_shouldThrowIndexOutOfBoundsException() throws IOException {
        byte[] b = new byte[4];
        base64InputStream.read(b, 0, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidBufferLength_shouldThrowIndexOutOfBoundsException() throws IOException {
        byte[] b = new byte[3];
        base64InputStream.read(b, 0, 4);
    }
    
    @Test
    public void testRead_withZeroLength_shouldReturnZero() throws IOException {
        byte[] b = new byte[4];
        int len = base64InputStream.read(b, 0, 0);
        assertEquals(0, len);
    }
    
    @Test
    public void testRead_withNonBase64Data_shouldReadRemainingData() throws IOException {
        // Custom MockInputStream with non-base64 data
        InputStream inputStream = new MockInputStream(new byte[] {0xFF, 0xEE, 0xDD, 0xCC});
        base64InputStream = new Base64InputStream(inputStream);
        byte[] expected = new byte[]{(byte) 0xFF, (byte) 0xEE, (byte) 0xDD, (byte) 0xCC};
        byte[] actual = new byte[4];
        int len = base64InputStream.read(actual);
        assertEquals(4, len);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testMarkSupported_shouldReturnFalse() {
        assertFalse(base64InputStream.markSupported());
    }
}