import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical Use Case
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Edge Cases
        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        input = "A==="; // Invalid padding
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical Use Case
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        
        // Edge Cases
        input = "";
        byte[] expectedEmpty = new byte[0];
        assertArrayEquals(expectedEmpty, Base64.base64ToByteArray(input));
        
        input = "A==="; // Invalid padding
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical Use Case
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expected = "SGVsbG8gV29ybGQ=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
        
        // Edge Cases
        byte[] emptyByteArray = new byte[0];
        String expectedEmpty = "";
        assertEquals(expectedEmpty, Base64.byteArrayToAltBase64(emptyByteArray));
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical Use Case
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expected = "SGVsbG8gV29ybGQ=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
        
        // Edge Cases
        byte[] emptyByteArray = new byte[0];
        String expectedEmpty = "";
        assertEquals(expectedEmpty, Base64.byteArrayToBase64(emptyByteArray));
    }

    @Test
    public void test_$23183() {
        // Typical Use Case
        char c = 'A';
        byte[] bb = new byte[128];
        bb['A'] = 65;
        assertEquals(65, Base64._$23183(c, bb));
        
        // Edge Cases
        char invalidChar = '!';
        assertThrows(IllegalArgumentException.class, () -> Base64._$23183(invalidChar, bb));
    }
}