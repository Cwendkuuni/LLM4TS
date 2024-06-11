import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Test with valid base64 encoded string
        String encodedString = "aGVsbG8gd29ybGQ=";
        byte[] expected = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(encodedString));
        
        // Test with invalid base64 encoded string
        String invalidEncodedString = "aGVsbG8gd29ybGQ"; // missing padding '=' at the end
        try {
            Base64.altBase64ToByteArray(invalidEncodedString);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Test with valid base64 encoded string
        String encodedString = "aGVsbG8gd29ybGQ=";
        byte[] expected = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.base64ToByteArray(encodedString));
        
        // Test with invalid base64 encoded string
        String invalidEncodedString = "aGVsbG8gd29ybGQ"; // missing padding '=' at the end
        try {
            Base64.base64ToByteArray(invalidEncodedString);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] byteArray = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "aGVsbG8gd29ybGQ=";
        assertEquals(expected, Base64.byteArrayToAltBase64(byteArray));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] byteArray = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "aGVsbG8gd29ybGQ=";
        assertEquals(expected, Base64.byteArrayToBase64(byteArray));
    }

    @Test
    public void testMain() {
        // Not testing the main method as it only contains print statements
    }
}