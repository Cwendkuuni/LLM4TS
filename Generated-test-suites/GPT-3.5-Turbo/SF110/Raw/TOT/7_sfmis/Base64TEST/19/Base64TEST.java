import org.junit.*;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical Use Case
        String input1 = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput1 = "Hello world".getBytes();
        assertArrayEquals(expectedOutput1, Base64.altBase64ToByteArray(input1));
        
        // Edge Case (Empty String)
        String input2 = "";
        byte[] expectedOutput2 = new byte[0];
        assertArrayEquals(expectedOutput2, Base64.altBase64ToByteArray(input2));
        
        // Error Scenario (String length not multiple of four)
        String input3 = "SGVsbG8gd29ybGQ";
        try {
            Base64.altBase64ToByteArray(input3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical Use Case
        String input1 = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput1 = "Hello world".getBytes();
        assertArrayEquals(expectedOutput1, Base64.base64ToByteArray(input1));
        
        // Edge Case (Empty String)
        String input2 = "";
        byte[] expectedOutput2 = new byte[0];
        assertArrayEquals(expectedOutput2, Base64.base64ToByteArray(input2));
        
        // Error Scenario (String length not multiple of four)
        String input3 = "SGVsbG8gd29ybGQ";
        try {
            Base64.base64ToByteArray(input3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical Use Case
        byte[] input1 = "Hello world".getBytes();
        String expectedOutput1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expectedOutput1, Base64.byteArrayToAltBase64(input1));
        
        // Edge Case (Empty byte array)
        byte[] input2 = new byte[0];
        String expectedOutput2 = "";
        assertEquals(expectedOutput2, Base64.byteArrayToAltBase64(input2));
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical Use Case
        byte[] input1 = "Hello world".getBytes();
        String expectedOutput1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expectedOutput1, Base64.byteArrayToBase64(input1));
        
        // Edge Case (Empty byte array)
        byte[] input2 = new byte[0];
        String expectedOutput2 = "";
        assertEquals(expectedOutput2, Base64.byteArrayToBase64(input2));
    }
}