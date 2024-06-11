import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = { 65, 66, 67 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        assertEquals(65, base64InputStream.read()); // Test reading first byte
        assertEquals(66, base64InputStream.read()); // Test reading second byte
        assertEquals(67, base64InputStream.read()); // Test reading third byte
        assertEquals(-1, base64InputStream.read()); // Test end of file
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = { 65, 66, 67 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] buffer = new byte[3];
        assertEquals(3, base64InputStream.read(buffer, 0, 3)); // Test reading all bytes
        assertArrayEquals(data, buffer); // Verify data read matches input data
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read(null, 0, 1);
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

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        
        assertFalse(base64InputStream.markSupported());
    }
}