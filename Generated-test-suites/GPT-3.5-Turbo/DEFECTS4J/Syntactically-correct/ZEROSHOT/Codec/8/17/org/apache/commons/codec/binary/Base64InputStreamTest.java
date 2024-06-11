import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() {
        // Test reading one byte at a time
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQ=".getBytes()));
        try {
            int byte1 = inputStream.read();
            int byte2 = inputStream.read();
            int byte3 = inputStream.read();
            int byte4 = inputStream.read();
            int byte5 = inputStream.read();
            int byte6 = inputStream.read();
            int byte7 = inputStream.read();
            
            assertEquals(72, byte1);
            assertEquals(101, byte2);
            assertEquals(108, byte3);
            assertEquals(108, byte4);
            assertEquals(111, byte5);
            assertEquals(32, byte6);
            assertEquals(87, byte7);
            
            int endOfFile = inputStream.read();
            assertEquals(-1, endOfFile); // end of file
            
        } catch (IOException e) {
            fail("An IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadByteArray() {
        // Test reading multiple bytes into a byte array
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQ=".getBytes()));
        try {
            byte[] byteArray = new byte[5];
            int bytesRead = inputStream.read(byteArray, 0, 5);
            
            assertEquals(5, bytesRead);
            assertEquals(72, byteArray[0]);
            assertEquals(101, byteArray[1]);
            assertEquals(108, byteArray[2]);
            assertEquals(108, byteArray[3]);
            assertEquals(111, byteArray[4]);
            
            int endOfFile = inputStream.read();
            assertEquals(-1, endOfFile); // end of file
            
        } catch (IOException e) {
            fail("An IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadByteArrayOffsetLength() {
        // Test reading bytes into a byte array with a specific offset and length
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQ=".getBytes()));
        try {
            byte[] byteArray = new byte[8];
            int bytesRead = inputStream.read(byteArray, 2, 5);
            
            assertEquals(5, bytesRead);
            assertEquals(0, byteArray[0]);
            assertEquals(0, byteArray[1]);
            assertEquals(72, byteArray[2]);
            assertEquals(101, byteArray[3]);
            assertEquals(108, byteArray[4]);
            assertEquals(108, byteArray[5]);
            assertEquals(111, byteArray[6]);
            assertEquals(0, byteArray[7]);
            
            int endOfFile = inputStream.read();
            assertEquals(-1, endOfFile); // end of file
            
        } catch (IOException e) {
            fail("An IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testMarkSupported() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQ=".getBytes()));
        assertFalse(inputStream.markSupported());
    }
}