import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = {65, 66, 67}; // The ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertEquals(65, base64InputStream.read()); // Testing reading the first byte
        assertEquals(66, base64InputStream.read()); // Testing reading the second byte
        assertEquals(67, base64InputStream.read()); // Testing reading the third byte
        assertEquals(-1, base64InputStream.read()); // Testing end of stream
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] inputBytes = {65, 66, 67}; // The ASCII values for 'A', 'B', 'C'
        byte[] buffer = new byte[3];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertEquals(3, base64InputStream.read(buffer, 0, 3)); // Reading all bytes into buffer
        assertArrayEquals(new byte[] {65, 66, 67}, buffer); // Checking if buffer has correct data
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(null, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(new byte[1], 0, -1);
    }

}