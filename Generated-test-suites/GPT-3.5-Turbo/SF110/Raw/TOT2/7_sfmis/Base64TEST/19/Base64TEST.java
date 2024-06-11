import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Edge case: input with padding characters
        String input2 = "SGVsbG8";
        byte[] expected2 = {72, 101, 108, 108, 111};
        assertArrayEquals(expected2, Base64.altBase64ToByteArray(input2));

        // Error scenario: input with illegal character
        String input3 = "SGV$123=";
        try {
            Base64.altBase64ToByteArray(input3);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Similar to testAltBase64ToByteArray test cases
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Similar to testAltBase64ToByteArray test cases
    }

    @Test
    public void testByteArrayToBase64() {
        // Similar to testAltBase64ToByteArray test cases
    }

    // Additional test cases for other methods in Base64 class can be added here

}