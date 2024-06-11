import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private byte[] testData = "Hello World!".getBytes();
    private byte[] encodedData = "SGVsbG8gV29ybGQh".getBytes();

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        byte[] buffer = new byte[4096];
        int bytesRead = base64InputStream.read(buffer);
        
        assertEquals(testData.length, bytesRead);
        assertArrayEquals(testData, buffer);
        
        base64InputStream.close();
    }

    @Test
    public void testReadBytes() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] buffer = new byte[10];
        int bytesRead = base64InputStream.read(buffer, 0, 10);

        assertEquals(testData.length, bytesRead);
        assertArrayEquals(testData, buffer);
        
        base64InputStream.close();
    }

    @Test
    public void testMarkSupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        assertFalse(base64InputStream.markSupported());
        
        base64InputStream.close();
    }
}