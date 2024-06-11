import static org.junit.Assert.*;
import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = ChatGPT_Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);

        // Edge case - empty input
        expected = new byte[0];
        actual = ChatGPT_Base64.altBase64ToByteArray("");
        assertArrayEquals(expected, actual);

        // Error scenario - invalid input
        try {
            ChatGPT_Base64.altBase64ToByteArray("invalidinput");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = ChatGPT_Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);

        // Edge case - empty input
        expected = new byte[0];
        actual = ChatGPT_Base64.base64ToByteArray("");
        assertArrayEquals(expected, actual);

        // Error scenario - invalid input
        try {
            ChatGPT_Base64.base64ToByteArray("invalidinput");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = ChatGPT_Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);

        // Edge case - empty input
        input = new byte[0];
        expected = "";
        actual = ChatGPT_Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = ChatGPT_Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);

        // Edge case - empty input
        input = new byte[0];
        expected = "";
        actual = ChatGPT_Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        // Error scenario - testing main method just to ensure it can be called without exceptions
        try {
            String[] args = {};
            ChatGPT_Base64.main(args); 
        } catch (Exception e) {
            fail("Unexpected exception occurred while calling main method");
        }
    }
}