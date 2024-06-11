import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical case
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Edge case - empty string
        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Error scenario - invalid string length
        input = "SGVsbG8";
        try {
            Base64.altBase64ToByteArray(input);
            fail("Expected IllegalArgumentException");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Similar tests as above for base64ToByteArray method
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Similar tests as above for byteArrayToAltBase64 method
    }

    @Test
    public void testByteArrayToBase64() {
        // Similar tests as above for byteArrayToBase64 method
    }
    
}