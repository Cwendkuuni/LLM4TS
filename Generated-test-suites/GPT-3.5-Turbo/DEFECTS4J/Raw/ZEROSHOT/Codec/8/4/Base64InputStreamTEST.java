import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        try {
            byte[] input = { 65, 66, 67, 68, 69, 70, 71, 72 };
            InputStream inputStream = new ByteArrayInputStream(input);
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            
            int expected = 65;
            int actual = base64InputStream.read();
            
            assertEquals(expected, actual);
            
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadWithEmptyBuffer() {
        try {
            byte[] input = {};
            InputStream inputStream = new ByteArrayInputStream(input);
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            
            int expected = -1;
            int actual = base64InputStream.read();
            
            assertEquals(expected, actual);
            
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadWithLargeBuffer() {
        try {
            byte[] input = new byte[1000000];
            new Random().nextBytes(input);
            
            InputStream inputStream = new ByteArrayInputStream(input);
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            
            byte[] output = new byte[100000];
            int expected = input.length;
            int actual = base64InputStream.read(output, 0, output.length);
            
            assertEquals(expected, actual);
            
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadWithInvalidOffset() {
        try {
            byte[] input = { 65, 66, 67, 68, 69, 70, 71, 72 };
            InputStream inputStream = new ByteArrayInputStream(input);
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            
            byte[] output = new byte[8];
            int offset = 9;
            int len = 8;
            
            base64InputStream.read(output, offset, len);
            
            fail("IndexOutOfBoundsException expected");
            
        } catch (IndexOutOfBoundsException e) {
            // Success
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadWithInvalidLength() {
        try {
            byte[] input = { 65, 66, 67, 68, 69, 70, 71, 72 };
            InputStream inputStream = new ByteArrayInputStream(input);
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            
            byte[] output = new byte[8];
            int offset = 0;
            int len = 9;
            
            base64InputStream.read(output, offset, len);
            
            fail("IndexOutOfBoundsException expected");
            
        } catch (IndexOutOfBoundsException e) {
            // Success
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadWithNullBuffer() {
        try {
            byte[] input = { 65, 66, 67, 68, 69, 70, 71, 72 };
            InputStream inputStream = new ByteArrayInputStream(input);
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            
            byte[] output = null;
            int offset = 0;
            int len = 8;
            
            base64InputStream.read(output, offset, len);
            
            fail("NullPointerException expected");
            
        } catch (NullPointerException e) {
            // Success
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testMarkSupported() {
        byte[] input = { 65, 66, 67, 68, 69, 70, 71, 72 };
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        boolean expected = false;
        boolean actual = base64InputStream.markSupported();
        
        assertEquals(expected, actual);
    }
}