
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64InputStream;

public class Base64InputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] data = { 65, 66, 67, 68, 69 }; // "ABCDE"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int byte1 = base64InputStream.read(); // "A"
        
        assertEquals(65, byte1);
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74 }; // "ABCDEFGHIJ"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, 5); // "ABCDE"
        
        assertArrayEquals(new byte[] { 65, 66, 67, 68, 69 }, buffer);
        assertEquals(5, bytesRead);
        
        base64InputStream.close();
    }
}
