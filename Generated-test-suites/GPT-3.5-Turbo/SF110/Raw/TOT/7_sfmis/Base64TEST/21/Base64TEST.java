import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical case: String length is a multiple of four
        String input1 = "SGVsbG8gd29ybGQ=";
        byte[] expected1 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        assertArrayEquals(expected1, Base64.altBase64ToByteArray(input1));

        // Edge case: String length is zero
        String input2 = "";
        byte[] expected2 = new byte[0];
        assertArrayEquals(expected2, Base64.altBase64ToByteArray(input2));

        // Error scenario: String length is not a multiple of four
        String input3 = "SGVsbG8gd29ybG==";
        try {
            Base64.altBase64ToByteArray(input3);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical case: String length is a multiple of four
        String input1 = "SGVsbG8gd29ybGQ=";
        byte[] expected1 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        assertArrayEquals(expected1, Base64.base64ToByteArray(input1));

        // Edge case: String length is zero
        String input2 = "";
        byte[] expected2 = new byte[0];
        assertArrayEquals(expected2, Base64.base64ToByteArray(input2));

        // Error scenario: String length is not a multiple of four
        String input3 = "SGVsbG8gd29ybG==";
        try {
            Base64.base64ToByteArray(input3);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical case: byte array length is a multiple of three
        byte[] input1 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expected1, Base64.byteArrayToAltBase64(input1));

        // Edge case: byte array length is zero
        byte[] input2 = new byte[0];
        String expected2 = "";
        assertEquals(expected2, Base64.byteArrayToAltBase64(input2));
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical case: byte array length is a multiple of three
        byte[] input1 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expected1, Base64.byteArrayToBase64(input1));

        // Edge case: byte array length is zero
        byte[] input2 = new byte[0];
        String expected2 = "";
        assertEquals(expected2, Base64.byteArrayToBase64(input2));
    }
}