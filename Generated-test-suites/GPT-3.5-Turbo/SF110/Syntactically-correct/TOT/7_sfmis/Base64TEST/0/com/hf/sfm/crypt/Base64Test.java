import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case: all characters are valid base64 characters
        String encodedString = "SGVsbG8=";
        byte[] expectedByteArray = {72, 101, 108, 108, 111};
        assertArrayEquals(expectedByteArray, Base64.altBase64ToByteArray(encodedString));
        
        // Edge case: empty string
        encodedString = "";
        expectedByteArray = new byte[0];
        assertArrayEquals(expectedByteArray, Base64.altBase64ToByteArray(encodedString));
        
        // Error scenario: string length is not multiple of four
        encodedString = "SGVsbG8"; // missing "=" at the end
        try {
            Base64.altBase64ToByteArray(encodedString);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Typical use case: all characters are valid base64 characters
        String encodedString = "SGVsbG8=";
        byte[] expectedByteArray = {72, 101, 108, 108, 111};
        assertArrayEquals(expectedByteArray, Base64.base64ToByteArray(encodedString));
        
        // Edge case: empty string
        encodedString = "";
        expectedByteArray = new byte[0];
        assertArrayEquals(expectedByteArray, Base64.base64ToByteArray(encodedString));
        
        // Error scenario: string length is not multiple of four
        encodedString = "SGVsbG8"; // missing "=" at the end
        try {
            Base64.base64ToByteArray(encodedString);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case: array contains valid byte values
        byte[] byteArray = {72, 101, 108, 108, 111};
        String expectedEncodedString = "SGVsbG8=";
        assertEquals(expectedEncodedString, Base64.byteArrayToAltBase64(byteArray));
        
        // Edge case: empty array
        byteArray = new byte[0];
        expectedEncodedString = "";
        assertEquals(expectedEncodedString, Base64.byteArrayToAltBase64(byteArray));
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Typical use case: array contains valid byte values
        byte[] byteArray = {72, 101, 108, 108, 111};
        String expectedEncodedString = "SGVsbG8=";
        assertEquals(expectedEncodedString, Base64.byteArrayToBase64(byteArray));
        
        // Edge case: empty array
        byteArray = new byte[0];
        expectedEncodedString = "";
        assertEquals(expectedEncodedString, Base64.byteArrayToBase64(byteArray));
    }
}