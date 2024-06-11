import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Test case 1: reading a single byte from an encoded stream
        // Given an input stream with encoded data 
        // When read method is called
        // Then the byte is decoded and returned as an integer in the range 0 to 255
        // And -1 is returned if EOF has been reached
        Base64InputStream inputStream1 = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gd29ybGQ=".getBytes()), false);
        assertEquals(72, inputStream1.read());
        assertEquals(101, inputStream1.read());
        assertEquals(108, inputStream1.read());
        assertEquals(108, inputStream1.read());
        assertEquals(111, inputStream1.read());
        assertEquals(32, inputStream1.read());
        assertEquals(119, inputStream1.read());
        assertEquals(111, inputStream1.read());
        assertEquals(114, inputStream1.read());
        assertEquals(108, inputStream1.read());
        assertEquals(100, inputStream1.read());
        assertEquals(-1, inputStream1.read());
        
        // Test case 2: reading multiple bytes from an encoded stream 
        // Given an input stream with encoded data
        // When read method is called with a byte array, offset, and length
        // Then the specified number of bytes is read and stored in the byte array starting from the specified offset
        Base64InputStream inputStream2 = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gd29ybGQ=".getBytes()), false);
        byte[] buffer2 = new byte[10];
        int bytesRead2 = inputStream2.read(buffer2, 2, 5);
        assertEquals(5, bytesRead2);
        assertEquals(0, buffer2[0]);
        assertEquals(0, buffer2[1]);
        assertEquals(72, buffer2[2]);
        assertEquals(101, buffer2[3]);
        assertEquals(108, buffer2[4]);
        assertEquals(108, buffer2[5]);
        assertEquals(111, buffer2[6]);
        assertEquals(0, buffer2[7]);
        assertEquals(0, buffer2[8]);
        assertEquals(0, buffer2[9]);

        // Test case 3: reading from an encoded stream with null input
        // Given an input stream with null input
        // When read method is called 
        // Then NullPointerException is thrown
        Base64InputStream inputStream3 = new Base64InputStream(null, false);
        try {
            inputStream3.read();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {}
        
        // Test case 4: reading from an encoded stream with negative offset and length
        // Given an input stream with encoded data
        // When read method is called with a negative offset and length
        // Then IndexOutOfBoundsException is thrown
        Base64InputStream inputStream4 = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gd29ybGQ=".getBytes()), false);
        byte[] buffer4 = new byte[5];
        try {
            inputStream4.read(buffer4, -1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {}
        
        // Test case 5: reading from an encoded stream with offset and length greater than buffer size
        // Given an input stream with encoded data
        // When read method is called with an offset and length greater than the size of the buffer
        // Then IndexOutOfBoundsException is thrown
        Base64InputStream inputStream5 = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gd29ybGQ=".getBytes()), false);
        byte[] buffer5 = new byte[5];
        try {
            inputStream5.read(buffer5, 0, 6);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {}
    }

    @Test
    public void testMarkSupported() {
        // Test case 1: mark is not supported
        // Given a Base64InputStream
        // When markSupported method is called
        // Then false is returned
        Base64InputStream inputStream1 = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gd29ybGQ=".getBytes()), false);
        assertFalse(inputStream1.markSupported());
    }
}