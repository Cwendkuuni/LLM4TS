import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {
    private byte[] sampleData = "Hello, World!".getBytes();
    
    @Test
    public void testBase64InputStream_Constructor1() {
        InputStream inputStream = new ByteArrayInputStream(sampleData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertNotNull(base64InputStream);
    }
    
    @Test
    public void testBase64InputStream_Constructor2() {
        InputStream inputStream = new ByteArrayInputStream(sampleData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertNotNull(base64InputStream);
    }
    
    @Test
    public void testBase64InputStream_Constructor3() {
        InputStream inputStream = new ByteArrayInputStream(sampleData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());
        assertNotNull(base64InputStream);
    }
    
    @Test
    public void testRead_WithValidData() {
        InputStream inputStream = new ByteArrayInputStream(sampleData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int data = base64InputStream.read();
        assertEquals('H', data);
    }
    
    @Test
    public void testRead_WithEmptyInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int data = base64InputStream.read();
        assertEquals(-1, data);
    }
    
    @Test(expected = IOException.class)
    public void testRead_IOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(sampleData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        inputStream.close();
        base64InputStream.read();
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_WithInvalidOffset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(sampleData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] data = new byte[10];
        base64InputStream.read(data, -1, 5);
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(sampleData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }
}