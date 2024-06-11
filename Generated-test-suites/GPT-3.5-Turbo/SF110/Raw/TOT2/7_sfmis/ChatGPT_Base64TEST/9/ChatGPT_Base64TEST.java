import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge cases
        // Testing empty input
        assertArrayEquals(new byte[0], Base64.altBase64ToByteArray(""));
        
        // Testing invalid input
        assertNull(Base64.altBase64ToByteArray("invalidInput"));
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge cases
        // Testing empty input
        assertArrayEquals(new byte[0], Base64.base64ToByteArray(""));
        
        // Testing invalid input
        assertNull(Base64.base64ToByteArray("invalidInput"));
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        // Edge cases
        // Testing empty input
        assertEquals("", Base64.byteArrayToAltBase64(new byte[0]));
        
        // Testing null input
        assertNull(Base64.byteArrayToAltBase64(null));
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Edge cases
        // Testing empty input
        assertEquals("", Base64.byteArrayToBase64(new byte[0]));
        
        // Testing null input
        assertNull(Base64.byteArrayToBase64(null));
    }

    @Test
    public void testMain() {
        // Just test that the method can be called without throwing an exception
        String[] args = {};
        Base64.main(args); 
    }
}