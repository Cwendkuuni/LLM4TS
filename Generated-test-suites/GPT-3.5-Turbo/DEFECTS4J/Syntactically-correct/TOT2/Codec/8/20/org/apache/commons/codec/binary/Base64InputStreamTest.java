import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor1() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        assertNotNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamConstructor2() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        
        assertNotNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamConstructor3() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        byte[] lineSeparator = "\r\n".getBytes();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, lineSeparator);
        
        assertNotNull(base64InputStream);
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int data = base64InputStream.read();
        
        assertEquals(116, data);
    }

    @Test
    public void testReadByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] data = new byte[10];
        int length = base64InputStream.read(data, 0, 4);
        
        assertEquals(4, length);
        assertEquals(116, data[0]);
        assertEquals(101, data[1]);
        assertEquals(115, data[2]);
        assertEquals(116, data[3]);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidOffsetInRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] data = new byte[10];
        base64InputStream.read(data, 11, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidLenInRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] data = new byte[10];
        base64InputStream.read(data, 0, 11);
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        assertFalse(base64InputStream.markSupported());
    }
}