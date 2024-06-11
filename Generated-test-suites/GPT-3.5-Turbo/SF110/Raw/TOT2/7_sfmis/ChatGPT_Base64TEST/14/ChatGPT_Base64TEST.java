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

    @Test
    public void testAltBase64ToByteArray_NullInput() {
        byte[] actual = Base64.altBase64ToByteArray(null);
        assertNull(actual);
    }

    @Test
    public void testBase64ToByteArray_EmptyInput() {
        byte[] actual = Base64.base64ToByteArray("");
        assertEquals(0, actual.length);
    }

    @Test
    public void testByteArrayToAltBase64_EmptyArray() {
        byte[] input = {};
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals("", actual);
    }

    @Test
    public void testByteArrayToBase64_NullArray() {
        String actual = Base64.byteArrayToBase64(null);
        assertEquals("", actual);
    }
}