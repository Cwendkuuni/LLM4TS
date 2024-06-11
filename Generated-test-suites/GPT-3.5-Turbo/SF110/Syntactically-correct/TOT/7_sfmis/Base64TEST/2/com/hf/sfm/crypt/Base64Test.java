import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String s1 = "SGVsbG8gd29ybGQ=";
        byte[] expected1 = "Hello world".getBytes();
        assertArrayEquals(expected1, Base64.altBase64ToByteArray(s1));

        // Edge case - empty string
        String s2 = "";
        byte[] expected2 = new byte[0];
        assertArrayEquals(expected2, Base64.altBase64ToByteArray(s2));

        // Error scenario - string length not multiple of four
        String s3 = "SGVsbG8gd29ybG";
        try {
            Base64.altBase64ToByteArray(s3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        String s1 = "SGVsbG8gd29ybGQ=";
        byte[] expected1 = "Hello world".getBytes();
        assertArrayEquals(expected1, Base64.base64ToByteArray(s1));

        // Edge case - empty string
        String s2 = "";
        byte[] expected2 = new byte[0];
        assertArrayEquals(expected2, Base64.base64ToByteArray(s2));

        // Error scenario - string length not multiple of four
        String s3 = "SGVsbG8gd29ybG";
        try {
            Base64.base64ToByteArray(s3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] b1 = "Hello world".getBytes();
        String expected1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expected1, Base64.byteArrayToAltBase64(b1));

        // Edge case - empty byte array
        byte[] b2 = new byte[0];
        String expected2 = "";
        assertEquals(expected2, Base64.byteArrayToAltBase64(b2));
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] b1 = "Hello world".getBytes();
        String expected1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expected1, Base64.byteArrayToBase64(b1));

        // Edge case - empty byte array
        byte[] b2 = new byte[0];
        String expected2 = "";
        assertEquals(expected2, Base64.byteArrayToBase64(b2));
    }

    @Test
    public void testInvalidCharacter() {
        // Error scenario - invalid character in base64 string
        String s = "SGVs bG8gd29ybGQ=";
        try {
            Base64.base64ToByteArray(s);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal character b", e.getMessage());
        }
    }
}