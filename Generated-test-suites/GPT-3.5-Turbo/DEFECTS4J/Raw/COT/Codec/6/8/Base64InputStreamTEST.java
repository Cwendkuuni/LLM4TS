import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() {
        byte[] data = {65, 66, 67};
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream b64in = new Base64InputStream(in);
        try {
            assertEquals(65, b64in.read());
            assertEquals(66, b64in.read());
            assertEquals(67, b64in.read());
            assertEquals(-1, b64in.read());
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadByteArray() {
        byte[] data = {65, 66, 67};
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream b64in = new Base64InputStream(in);
        byte[] output = new byte[3];
        try {
            int bytesRead = b64in.read(output, 0, 3);
            assertEquals(3, bytesRead);
            assertArrayEquals(data, output);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadByteArrayWithOffset() {
        byte[] data = {65, 66, 67};
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream b64in = new Base64InputStream(in);
        byte[] output = new byte[5];
        try {
            int bytesRead = b64in.read(output, 1, 3);
            assertEquals(3, bytesRead);
            assertArrayEquals(new byte[]{0, 65, 66, 67, 0}, output);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadByteArrayWithInvalidOffset() {
        byte[] data = {65, 66, 67};
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream b64in = new Base64InputStream(in);
        byte[] output = new byte[5];
        try {
            b64in.read(output, -1, 3);
            fail("Expected IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // expected
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadByteArrayWithInvalidLength() {
        byte[] data = {65, 66, 67};
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream b64in = new Base64InputStream(in);
        byte[] output = new byte[5];
        try {
            b64in.read(output, 0, 6);
            fail("Expected IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // expected
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadByteArrayWithNullArray() {
        byte[] data = {65, 66, 67};
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream b64in = new Base64InputStream(in);
        try {
            b64in.read(null, 0, 3);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // expected
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testMarkSupported() {
        byte[] data = {65, 66, 67};
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream b64in = new Base64InputStream(in);
        assertFalse(b64in.markSupported());
    }
}