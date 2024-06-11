import static org.junit.Assert.*;
import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
    }

    // Edge case: Testing empty input for altBase64ToByteArray
    @Test
    public void testAltBase64ToByteArrayEmptyInput() {
        byte[] expected = {};
        byte[] actual = Base64.altBase64ToByteArray("");
        assertArrayEquals(expected, actual);
    }

    // Exception handling: Testing null input for base64ToByteArray
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayNullInput() {
        Base64.base64ToByteArray(null);
    }

}