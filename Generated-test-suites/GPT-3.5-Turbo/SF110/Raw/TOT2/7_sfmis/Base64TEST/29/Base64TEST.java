import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        // Add more test cases for edge cases and error scenarios
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        // Add more test cases for edge cases and error scenarios
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expected = "SGVsbG8gV29ybGQ=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
        // Add more test cases for edge cases and error scenarios
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expected = "SGVsbG8gV29ybGQ=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
        // Add more test cases for edge cases and error scenarios
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCharacterException() {
        Base64._$23183('&', Base64._$23169);
    }
}