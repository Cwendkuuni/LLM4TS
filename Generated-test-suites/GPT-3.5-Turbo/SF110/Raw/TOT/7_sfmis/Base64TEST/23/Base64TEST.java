import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String input1 = "SGVsbG8gd29ybGQ=";
        byte[] expected1 = "Hello world".getBytes();
        assertArrayEquals(expected1, Base64.altBase64ToByteArray(input1));

        // Edge case: empty string
        String input2 = "";
        byte[] expected2 = new byte[0];
        assertArrayEquals(expected2, Base64.altBase64ToByteArray(input2));

        // Error scenario: invalid input (not a multiple of four)
        String input3 = "SGVsbG8gd29ybGQ";
        try {
            Base64.altBase64ToByteArray(input3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        String input1 = "SGVsbG8gd29ybGQ=";
        byte[] expected1 = "Hello world".getBytes();
        assertArrayEquals(expected1, Base64.base64ToByteArray(input1));

        // Edge case: empty string
        String input2 = "";
        byte[] expected2 = new byte[0];
        assertArrayEquals(expected2, Base64.base64ToByteArray(input2));

        // Error scenario: invalid input (not a multiple of four)
        String input3 = "SGVsbG8gd29ybGQ";
        try {
            Base64.base64ToByteArray(input3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input1 = "Hello world".getBytes();
        String expected1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expected1, Base64.byteArrayToAltBase64(input1));

        // Edge case: empty array
        byte[] input2 = new byte[0];
        String expected2 = "";
        assertEquals(expected2, Base64.byteArrayToAltBase64(input2));
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input1 = "Hello world".getBytes();
        String expected1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expected1, Base64.byteArrayToBase64(input1));

        // Edge case: empty array
        byte[] input2 = new byte[0];
        String expected2 = "";
        assertEquals(expected2, Base64.byteArrayToBase64(input2));
    }
}