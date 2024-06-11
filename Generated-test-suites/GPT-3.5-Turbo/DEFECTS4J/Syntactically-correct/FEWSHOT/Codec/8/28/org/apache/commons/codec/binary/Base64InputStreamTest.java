import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

public class Base64InputStreamTest {
    @Test
    public void testRead() throws IOException {
        byte[] data = "Hello, world!".getBytes();
        byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int readByte;
        for (byte aData : data) {
            readByte = base64InputStream.read();
            assertEquals(aData, (byte) readByte);
        }
        readByte = base64InputStream.read();
        assertEquals(-1, readByte);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = "Hello, world!".getBytes();
        byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] readBytes = new byte[6];
        int bytesRead = base64InputStream.read(readBytes, 1, 4);
        assertEquals(4, bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            assertEquals(data[i], readBytes[i + 1]);
        }
        bytesRead = base64InputStream.read(readBytes, 2, 3);
        assertEquals(2, bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            assertEquals(data[i + 4], readBytes[i + 2]);
        }
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }
}