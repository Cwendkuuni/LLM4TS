import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testConstructor() {
        // Test default constructor
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertNotNull(base64InputStream);
        
        // Test constructor with encoding
        Base64InputStream encodeStream = new Base64InputStream(inputStream, true);
        assertNotNull(encodeStream);
        
        // Test constructor with line length and separator
        byte[] separator = {'\r', '\n'};
        Base64InputStream lineStream = new Base64InputStream(inputStream, true, 76, separator);
        assertNotNull(lineStream);
    }

    @Test
    public void testReadSingleByte() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int data = base64InputStream.read();
        assertEquals('T', (char) data);
    }

    @Test
    public void testReadByteArray() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        byte[] buffer = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int bytesRead = base64InputStream.read(buffer, 0, 4);
        assertEquals(4, bytesRead);
        assertEquals('T', (char) buffer[0]);
    }

    @Test
    public void testEdgeCases() {
        // Test reading from an empty InputStream
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStreamEmpty = new Base64InputStream(emptyStream);
        assertEquals(-1, base64InputStreamEmpty.read());

        // Test reading with offset and length beyond buffer size
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        byte[] buffer = new byte[2];
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(buffer, 3, 2));
    }

    @Test
    public void testIOExceptionHandling() {
        // Test NullPointerException for null byte array
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = null;
        assertThrows(NullPointerException.class, () -> base64InputStream.read(buffer, 0, 4));

        // Test IndexOutOfBoundsException for invalid offset
        buffer = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(buffer, -1, 4));

        // Test IndexOutOfBoundsException for invalid length
        assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(buffer, 0, -1));

        // Test IndexOutOfBoundsException for offset + length exceeding buffer size
        assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(buffer, 3, 2));
    }

}