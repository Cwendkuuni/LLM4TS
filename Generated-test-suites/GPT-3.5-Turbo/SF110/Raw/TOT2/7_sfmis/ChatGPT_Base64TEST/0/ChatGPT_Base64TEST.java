import static org.junit.Assert.*;

import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: Testing an empty input
        byte[] expectedEmpty = {};
        byte[] actualEmpty = Base64.altBase64ToByteArray("");
        assertArrayEquals(expectedEmpty, actualEmpty);
        
        // Exception handling: Passing a null input
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.altBase64ToByteArray(null);
        });
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Exception handling: Testing invalid base64 input
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.base64ToByteArray("123456");
        });
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: Testing an empty input array
        byte[] emptyInput = {};
        String expectedEmpty = "";
        String actualEmpty = Base64.byteArrayToAltBase64(emptyInput);
        assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: Testing an empty input array
        byte[] emptyInput = {};
        String expectedEmpty = "";
        String actualEmpty = Base64.byteArrayToBase64(emptyInput);
        assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testMain() {
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
        
        // Error scenario: Passing arguments to main method
        String[] argsWithError = {"arg1", "arg2"};
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.main(argsWithError);
        });
    }
}