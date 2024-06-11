import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Base64InputStream base64Input = new Base64InputStream(input);
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int b;
        while ((b = base64Input.read()) != -1) {
            output.write(b);
        }
        
        String decodedData = new String(output.toByteArray());
        assertEquals("Hello World", decodedData);
        
        base64Input.close();
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Base64InputStream base64Input = new Base64InputStream(input);
        
        byte[] output = new byte[5];
        int bytesRead = base64Input.read(output, 0, 5);
        String decodedData = new String(output, 0, bytesRead);
        assertEquals("Hello", decodedData);
        
        base64Input.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadNullArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input);
        
        base64Input.read(null, 0, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input);
        
        base64Input.read(new byte[5], -1, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input);
        
        base64Input.read(new byte[5], 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input);
        
        base64Input.read(new byte[5], 0, 10);
    }
    
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input);
        
        assertFalse(base64Input.markSupported());
    }
}