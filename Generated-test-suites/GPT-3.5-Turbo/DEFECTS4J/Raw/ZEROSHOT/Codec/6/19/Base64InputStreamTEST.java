import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Base64InputStreamTest {

    @Test
    void read_singleByte_ReturnsCorrectByte() throws IOException {
        byte[] testData = {65, 66, 67, 68, 69};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();

        assertEquals(65, result);
    }

    @Test
    void read_byteArray_ReturnsCorrectBytes() throws IOException {
        byte[] testData = {65, 66, 67, 68, 69};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[3];
        base64InputStream.read(result, 0, 3);

        assertArrayEquals(new byte[]{65, 66, 67}, result);
    }

    @Test
    void read_byteArrayOffset_ReturnsCorrectBytes() throws IOException {
        byte[] testData = {65, 66, 67, 68, 69};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[5];
        base64InputStream.read(result, 1, 3);

        assertArrayEquals(new byte[]{0, 65, 66, 67, 0}, result);
    }

    @Test
    void read_byteArrayOffsetLen_ReturnsCorrectBytes() throws IOException {
        byte[] testData = {65, 66, 67, 68, 69};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[5];
        base64InputStream.read(result, 1, 3);

        assertArrayEquals(new byte[]{0, 65, 66, 67, 0}, result);
    }

    @Test
    void markSupported_ReturnsFalse() {
        byte[] testData = {65, 66, 67, 68, 69};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        boolean result = base64InputStream.markSupported();

        assertFalse(result);
    }
}