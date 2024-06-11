import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead_singleByteSuccess() {
        byte[] data = new byte[]{65, 66, 67}; // ABC
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteInputStream);
        
        try {
            int result = base64InputStream.read();
            
            assertEquals(65, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testRead_partialByteArraySuccess() {
        byte[] data = new byte[]{65, 66, 67}; // ABC
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteInputStream);
        byte[] outputBuffer = new byte[3];
        
        try {
            int result = base64InputStream.read(outputBuffer, 0, 2);
            
            assertEquals(2, result);
            assertArrayEquals(new byte[]{65, 66, 0}, outputBuffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testRead_entireByteArraySuccess() {
        byte[] data = new byte[]{65, 66, 67}; // ABC
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteInputStream);
        byte[] outputBuffer = new byte[3];
        
        try {
            int result = base64InputStream.read(outputBuffer, 0, 3);
            
            assertEquals(3, result);
            assertArrayEquals(new byte[]{65, 66, 67}, outputBuffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testRead_invalidOffsetFailure() {
        byte[] data = new byte[]{65, 66, 67}; // ABC
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteInputStream);
        byte[] outputBuffer = new byte[3];
        
        try {
            base64InputStream.read(outputBuffer, -1, 2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (IndexOutOfBoundsException e) {
            // Pass
        }
    }
    
    @Test
    public void testRead_invalidLengthFailure() {
        byte[] data = new byte[]{65, 66, 67}; // ABC
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteInputStream);
        byte[] outputBuffer = new byte[3];
        
        try {
            base64InputStream.read(outputBuffer, 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (IndexOutOfBoundsException e) {
            // Pass
        }
    }
    
    @Test
    public void testRead_bufferTooSmallFailure() {
        byte[] data = new byte[]{65, 66, 67}; // ABC
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteInputStream);
        byte[] outputBuffer = new byte[2];
        
        try {
            int result = base64InputStream.read(outputBuffer, 0, 2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (IndexOutOfBoundsException e) {
            // Pass
        }
    }
    
    @Test
    public void testRead_eofSuccess() {
        byte[] data = new byte[]{65}; // A
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteInputStream);
        byte[] outputBuffer = new byte[3];
        
        try {
            int result = base64InputStream.read(outputBuffer, 0, 3);
            
            assertEquals(-1, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
}