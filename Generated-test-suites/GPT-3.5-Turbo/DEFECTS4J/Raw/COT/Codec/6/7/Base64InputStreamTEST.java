
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.commons.codec.binary.Base64InputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private byte[] data;
    private InputStream stream;

    @BeforeEach
    void setup() {
        data = "Hello, World!".getBytes();
        stream = new ByteArrayInputStream(data);
    }

    @Test
    void testReadOneByte() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(stream);
        int byteRead = inputStream.read();
        Assertions.assertEquals('H', byteRead);
        inputStream.close();
    }

    @Test
    void testReadBytes() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(stream);
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer);
        byte[] expectedBuffer = "Hello".getBytes();
        Assertions.assertArrayEquals(expectedBuffer, buffer);
        Assertions.assertEquals(5, bytesRead);
        inputStream.close();
    }

    @Test
    void testReadBytesWithOffsetAndLength() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(stream);
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 1, 8);
        byte[] expectedBuffer = {0, 'H', 'e', 'l', 'l', 'o', ',', ' ', 0, 0};
        Assertions.assertArrayEquals(expectedBuffer, buffer);
        Assertions.assertEquals(8, bytesRead);
        inputStream.close();
    }

    @Test
    void testMarkSupported() {
        Base64InputStream inputStream = new Base64InputStream(stream);
        boolean markSupported = inputStream.markSupported();
        Assertions.assertFalse(markSupported);
    }
}
