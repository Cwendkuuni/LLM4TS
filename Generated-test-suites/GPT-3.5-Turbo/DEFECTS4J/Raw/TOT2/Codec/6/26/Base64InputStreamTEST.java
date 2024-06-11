import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor() {
        InputStream in = new ByteArrayInputStream("TestString".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamRead() throws IOException {
        InputStream in = new ByteArrayInputStream("TestString".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        int result = base64InputStream.read();
        assertEquals(84, result); // ASCII value for 'T'
    }

    @Test
    public void testBase64InputStreamReadBuffer() throws IOException {
        InputStream in = new ByteArrayInputStream("TestString".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[4];
        int result = base64InputStream.read(buffer, 0, 4);
        assertEquals("Test".getBytes(), buffer);
        assertEquals(4, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamNullBuffer() throws IOException {
        InputStream in = new ByteArrayInputStream("TestString".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamInvalidOffset() throws IOException {
        InputStream in = new ByteArrayInputStream("TestString".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, -1, 4);
    }
    
    @Test
    public void testBase64InputStreamMarkNotSupported() {
        InputStream in = new ByteArrayInputStream("TestString".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertFalse(base64InputStream.markSupported());
    }

}