import org.junit.*;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // typical use case
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);
        
        // edge case - empty string
        s = "";
        expected = new byte[0];
        result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);
        
        // error scenario - string length not a multiple of four
        s = "SGVsbG8gd29ybG";
        try {
            result = Base64.altBase64ToByteArray(s);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testBase64ToByteArray() {
        // typical use case
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);
        
        // edge case - empty string
        s = "";
        expected = new byte[0];
        result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);
        
        // error scenario - string length not a multiple of four
        s = "SGVsbG8gd29ybG";
        try {
            result = Base64.base64ToByteArray(s);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // typical use case
        byte[] b = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToAltBase64(b);
        assertEquals(expected, result);
        
        // edge case - empty byte array
        b = new byte[0];
        expected = "";
        result = Base64.byteArrayToAltBase64(b);
        assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToBase64() {
        // typical use case
        byte[] b = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToBase64(b);
        assertEquals(expected, result);
        
        // edge case - empty byte array
        b = new byte[0];
        expected = "";
        result = Base64.byteArrayToBase64(b);
        assertEquals(expected, result);
    }
    
    @Test
    public void test_$23183() {
        // typical use case
        char c = 'H';
        byte[] bb = Base64._$23169;
        int expected = 72;
        int result = Base64._$23183(c, bb);
        assertEquals(expected, result);
        
        // error scenario - illegal character
        c = '!';
        try {
            result = Base64._$23183(c, bb);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}