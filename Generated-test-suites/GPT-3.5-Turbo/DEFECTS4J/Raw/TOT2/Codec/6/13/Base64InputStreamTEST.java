import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Typical case
        byte[] data = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        Assert.assertEquals(65, base64InputStream.read());
        Assert.assertEquals(66, base64InputStream.read());
        Assert.assertEquals(67, base64InputStream.read());
        Assert.assertEquals(-1, base64InputStream.read());
        
        base64InputStream.close();
    }

    @Test
    public void testReadBytes() throws IOException {
        // Typical case
        byte[] data = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[3];
        
        int bytesRead = base64InputStream.read(buffer, 0, 3);
        Assert.assertEquals(3, bytesRead);
        Assert.assertArrayEquals(data, buffer);
        
        base64InputStream.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadBytesWithNullBuffer() throws IOException {
        byte[] data = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read(null, 0, 3);
        base64InputStream.close();
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithInvalidOffset() throws IOException {
        byte[] data = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read(new byte[3], 4, 3);
        base64InputStream.close();
    }
    
    @Test(expected = IOException.class)
    public void testReadWithIOException() throws IOException {
        InputStream inputStream = new InputStream() {
            public int read() throws IOException {
                throw new IOException("Test Exception");
            }
        };
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read();
        base64InputStream.close();
    }

}