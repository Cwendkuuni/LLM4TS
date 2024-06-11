import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Test case: read the first byte
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0xF1, (byte) 0x92, (byte) 0x34});
        Base64InputStream base64Input = new Base64InputStream(input);
        int result = base64Input.read();
        assertEquals(0xF1, result);

        // Test case: read beyond the end of stream
        result = base64Input.read();
        assertEquals(-1, result);
        
        // Test case: read the first byte with encoding
        input = new ByteArrayInputStream(new byte[]{(byte) 0xF1, (byte) 0x92, (byte) 0x34});
        base64Input = new Base64InputStream(input, true);
        result = base64Input.read();
        assertEquals(0x2B, result);

        // Test case: read the first byte with decoding
        input = new ByteArrayInputStream(new byte[]{(byte) 0x2B, (byte) 0xEC, (byte) 0x29});
        base64Input = new Base64InputStream(input, false);
        result = base64Input.read();
        assertEquals(0x2B, result);
        
        // Test case: read beyond the end of stream with decoding
        result = base64Input.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        int offset = 1;
        int length = 3;

        // Test case: read with offset and length
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0xF1, (byte) 0x92, (byte) 0x34, (byte) 0x8D, (byte) 0x74});
        Base64InputStream base64Input = new Base64InputStream(input);
        int bytesRead = base64Input.read(buffer, offset, length);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, (byte) 0xF1, (byte) 0x92, (byte) 0x34, 0}, buffer);

        // Test case: read with offset and length beyond the end of stream
        bytesRead = base64Input.read(buffer, offset, length);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testMarkSupported() {
        // Test case: markSupported should return false
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0xF1, (byte) 0x92, (byte) 0x34});
        Base64InputStream base64Input = new Base64InputStream(input);
        assertFalse(base64Input.markSupported());
    }

}