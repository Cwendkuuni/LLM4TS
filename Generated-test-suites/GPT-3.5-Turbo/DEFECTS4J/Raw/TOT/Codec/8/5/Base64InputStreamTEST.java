import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

public class Base64InputStreamTest {

    private final String BASE64_ENCODED_STRING = "SGVsbG8gd29ybGQh";

    private final String BASE64_DECODED_STRING = "Hello world!";
    
    @Test
    public void testRead_singleByteDecoding() throws IOException {
        byte[] base64EncodedBytes = BASE64_ENCODED_STRING.getBytes();
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes));
        
        int character;
        StringBuilder decodedString = new StringBuilder();
        while ((character = inputStream.read()) != -1) {
            decodedString.append((char) character);
        }
        
        assertEquals(BASE64_DECODED_STRING, decodedString.toString());
        inputStream.close();
    }
    
    @Test
    public void testRead_byteArrayDecoding() throws IOException {
        byte[] base64EncodedBytes = BASE64_ENCODED_STRING.getBytes();
        byte[] decodedBytes = new byte[base64EncodedBytes.length];
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes));
        
        int bytesRead = inputStream.read(decodedBytes);
        
        assertEquals(BASE64_DECODED_STRING.length(), bytesRead);
        assertEquals(BASE64_DECODED_STRING, new String(decodedBytes));
        inputStream.close();
    }
    
    @Test
    public void testRead_byteArrayOffsetDecoding() throws IOException {
        byte[] base64EncodedBytes = BASE64_ENCODED_STRING.getBytes();
        byte[] decodedBytes = new byte[base64EncodedBytes.length + 5];
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes));
        
        int bytesRead = inputStream.read(decodedBytes, 5, 10);
        
        assertEquals(10, bytesRead);
        assertEquals(BASE64_DECODED_STRING.substring(0, 10), new String(decodedBytes, 5, 10));
        inputStream.close();
    }
    
    @Test
    public void testRead_byteArrayNegativeLengthDecoding() throws IOException {
        byte[] base64EncodedBytes = BASE64_ENCODED_STRING.getBytes();
        byte[] decodedBytes = new byte[base64EncodedBytes.length];
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes));
        
        int bytesRead = inputStream.read(decodedBytes, 0, -1);
        
        assertEquals(BASE64_DECODED_STRING.length(), bytesRead);
        assertEquals(BASE64_DECODED_STRING, new String(decodedBytes));
        inputStream.close();
    }
    
    @Test
    public void testRead_byteArrayInvalidOffsetDecoding() throws IOException {
        byte[] base64EncodedBytes = BASE64_ENCODED_STRING.getBytes();
        byte[] decodedBytes = new byte[base64EncodedBytes.length];
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes));
        
        int bytesRead = inputStream.read(decodedBytes, -1, 10);
        
        assertEquals(BASE64_DECODED_STRING.length(), bytesRead);
        assertEquals(BASE64_DECODED_STRING, new String(decodedBytes));
        inputStream.close();
    }
    
    @Test
    public void testRead_byteArrayInvalidOffsetAndLengthDecoding() throws IOException {
        byte[] base64EncodedBytes = BASE64_ENCODED_STRING.getBytes();
        byte[] decodedBytes = new byte[base64EncodedBytes.length];
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes));
        
        int bytesRead = inputStream.read(decodedBytes, -1, -1);
        
        assertEquals(BASE64_DECODED_STRING.length(), bytesRead);
        assertEquals(BASE64_DECODED_STRING, new String(decodedBytes));
        inputStream.close();
    }
    
    @Test
    public void testRead_zeroLengthByteDecoding() throws IOException {
        byte[] base64EncodedBytes = BASE64_ENCODED_STRING.getBytes();
        byte[] decodedBytes = new byte[base64EncodedBytes.length];
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes));
        
        int bytesRead = inputStream.read(decodedBytes, 0, 0);
        
        assertEquals(0, bytesRead);
        inputStream.close();
    }
    
    @Test
    public void testRead_markNotSupported() {
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        
        assertFalse(inputStream.markSupported());
    }
    
    @Test
    public void testConstructor_singleParameter() throws IOException {
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        
        assertNotNull(inputStream);
        inputStream.close();
    }
    
    @Test
    public void testConstructor_doubleParameter() throws IOException {
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        
        assertNotNull(inputStream);
        inputStream.close();
    }
    
    @Test
    public void testConstructor_tripleParameter() throws IOException {
        InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false, 76, new byte[] {13, 10});
        
        assertNotNull(inputStream);
        inputStream.close();
    }

}