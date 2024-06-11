import static org.junit.Assert.*;
import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: Empty input
        byte[] actualEmpty = Base64.altBase64ToByteArray("");
        assertEquals(0, actualEmpty.length);
        
        // Exception handling scenario: Null input
        try {
            byte[] actualNull = Base64.altBase64ToByteArray(null);
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: Invalid Base64 input
        try {
            byte[] actualInvalid = Base64.base64ToByteArray("inv@lid");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: Empty input
        byte[] emptyInput = {};
        String actualEmpty = Base64.byteArrayToAltBase64(emptyInput);
        assertEquals("", actualEmpty);
        
        // Error scenario: Input with negative values
        byte[] negativeInput = { -1, -2, -3, -4, -5, -6 };
        try {
            String actualNegative = Base64.byteArrayToAltBase64(negativeInput);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: Empty input
        byte[] emptyInput = {};
        String actualEmpty = Base64.byteArrayToBase64(emptyInput);
        assertEquals("", actualEmpty);
        
        // Error scenario: Input with values greater than 255
        byte[] highInput = { 256, 257, 258, 259 };
        try {
            String actualHigh = Base64.byteArrayToBase64(highInput);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testMain() {
        // Typical use case
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
    }
}