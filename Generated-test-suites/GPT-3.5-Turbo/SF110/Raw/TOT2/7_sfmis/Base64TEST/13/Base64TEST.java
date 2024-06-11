import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Edge case: Empty string
        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Error scenario: String length not multiple of four
        input = "abcde";
        try {
            Base64.altBase64ToByteArray(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        
        // Edge case: Empty string
        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        
        // Error scenario: String length not multiple of four
        input = "abcde";
        try {
            Base64.base64ToByteArray(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
        
        // Edge case: Empty byte array
        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
        
        // Edge case: Empty byte array
        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }
    
    @Test
    public void test_$23183() {
        // Typical use case
        char c = 'A';
        byte[] bb = new byte[256];
        bb['A'] = 10;
        int expected = 10;
        assertEquals(expected, Base64._$23183(c, bb));
        
        // Edge case: Illegal character
        c = '-';
        try {
            Base64._$23183(c, bb);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }
}