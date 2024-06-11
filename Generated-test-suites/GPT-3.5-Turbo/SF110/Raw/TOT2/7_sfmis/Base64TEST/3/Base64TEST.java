import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gV29ybGQh"; // "Hello World!"
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }
    
    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gV29ybGQh"; // "Hello World!"
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        String expected = "SGVsbG8gV29ybGQh"; // "Hello World!"
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        String expected = "SGVsbG8gV29ybGQh"; // "Hello World!"
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }
    
    // Additional tests for edge cases and error scenarios can be added here
    
}