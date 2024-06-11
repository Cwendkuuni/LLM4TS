import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream_DefaultConstruction() {
        InputStream input = new ByteArrayInputStream("RnJvbSBvZiBpbiB0aGUrQGVuc2VzYWw=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] buffer = new byte[5];
        int bytesRead;

        // Test reading data from the input stream
        bytesRead = base64InputStream.read(buffer, 0, 5);
        assertArrayEquals("Expected 5 bytes to be read", new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45}, buffer, 0, bytesRead);

        // Test reading all data from the input stream
        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals("Expected 8 bytes to be read", new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48}, buffer, 0, bytesRead);

        // Test reaching the end of the input stream
        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals("Expected -1 to be returned when reaching the end of the input stream", -1, bytesRead);

        // Clean up resources
        base64InputStream.close();
        input.close();
    }

    @Test
    public void testBase64InputStream_WithEncoding() {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAg".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        byte[] buffer = new byte[10];
        int bytesRead;

        // Test reading data from the input stream
        bytesRead = base64InputStream.read(buffer, 0, 5);
        assertArrayEquals("Expected 5 bytes to be read", new byte[]{(byte) 0x56, (byte) 0x49, (byte) 0x4c, (byte) 0x45, (byte) 0x53}, buffer, 0, bytesRead);

        // Test reading all data from the input stream
        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals("Expected 12 bytes to be read", new byte[]{(byte) 0x56, (byte) 0x49, (byte) 0x4c, (byte) 0x45, (byte) 0x53, (byte) 0x55, (byte) 0x4e, (byte) 0x49, (byte) 0x54, (byte) 0x2d, (byte) 0x42, (byte) 0x59}, buffer, 0, bytesRead);

        // Test reaching the end of the input stream
        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals("Expected -1 to be returned when reaching the end of the input stream", -1, bytesRead);

        // Clean up resources
        base64InputStream.close();
        input.close();
    }

    @Test
    public void testBase64InputStream_WithCustomLineLength() {
        InputStream input = new ByteArrayInputStream("RnJvbSBvZiBpbiB0aGUrQGVuc2VzYWw=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false, 50);

        byte[] buffer = new byte[5];
        int bytesRead;

        // Test reading data from the input stream
        bytesRead = base64InputStream.read(buffer, 0, 5);
        assertArrayEquals("Expected 5 bytes to be read", new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45}, buffer, 0, bytesRead);

        // Test reading all data from the input stream
        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals("Expected 8 bytes to be read", new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48}, buffer, 0, bytesRead);

        // Test reaching the end of the input stream
        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals("Expected -1 to be returned when reaching the end of the input stream", -1, bytesRead);

        // Test custom line length
        InputStream customLineLengthInput = new ByteArrayInputStream("RnJvbSBvZiBpbiB0aGUrQGVuc2VzYWw=\r\nUm9sbG93VGhlcmUo".getBytes());
        Base64InputStream customLineLengthBase64InputStream = new Base64InputStream(customLineLengthInput, false, 25, new byte[]{0x0D, 0x0A});

        buffer = new byte[7];
        bytesRead = base64InputStream.read(buffer, 0, 7);
        assertArrayEquals("Expected 7 bytes to be read", new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x0D, (byte) 0x0A}, buffer, 0, bytesRead);

        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals("Expected 11 bytes to be read", new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x0D, (byte) 0x0A, (byte) 0x55}, buffer, 0, bytesRead);

        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals("Expected -1 to be returned when reaching the end of the input stream", -1, bytesRead);

        // Clean up resources
        base64InputStream.close();
        input.close();
        customLineLengthBase64InputStream.close();
        customLineLengthInput.close();
    }

    @Test(expected = IOException.class)
    public void testBase64InputStream_WithInvalidInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        base64InputStream.close();
    }
}