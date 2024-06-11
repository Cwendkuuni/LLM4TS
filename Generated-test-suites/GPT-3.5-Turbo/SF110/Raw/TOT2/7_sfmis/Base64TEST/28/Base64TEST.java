import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Test typical input
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Test typical input
        String input = "U29ycnksIEkgY2Fubm90IGhlbHAgdG8gdGVzdA==";
        byte[] expected = {83, 111, 114, 114, 121, 44, 32, 73, 32, 99, 97, 110, 110, 111, 116, 32, 104, 101, 108, 112, 32, 116, 111, 32, 116, 101, 115, 116};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }
    
    @Test
    public void testByteToAltBase64() {
        // Test typical input
        byte[] input = {87, 111, 114, 108, 100};
        String expected = "V29ybGQ=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteToBase64() {
        // Test typical input
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }
    
    @Test
    public void testIllegalCharacterException() {
        // Test illegal character exception
        String input = "SGVsbG8+";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testStringLengthException() {
        // Test string length exception
        String input = "SGVsbadfG8";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }
}