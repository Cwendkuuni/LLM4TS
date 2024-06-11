import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray_TypicalCase() {
        String input = "SGVsbG8gV29ybGQh";
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }
    
    @Test
    public void testAltBase64ToByteArray_EmptyString() {
        String input = "";
        byte[] expected = {};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_StringLengthNotMultipleOfFour() {
        String input = "SGVsbG8"; // String length is not a multiple of four
        Base64.altBase64ToByteArray(input);
    }
    
    @Test
    public void testBase64ToByteArray_TypicalCase() {
        String input = "U29tZSB0ZXh0IHN0cmluZyE=";
        byte[] expected = {83, 111, 109, 101, 32, 116, 101, 120, 116, 32, 115, 116, 114, 105, 110, 103, 33};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }
    
    @Test
    public void testBase64ToByteArray_EmptyString() {
        String input = "";
        byte[] expected = {};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_StringLengthNotMultipleOfFour() {
        String input = "U29tZSB0ZXh0"; // String length is not a multiple of four
        Base64.base64ToByteArray(input);
    }
    
    @Test
    public void testByteArrayToAltBase64_TypicalCase() {
        byte[] input = {83, 111, 109, 101, 32, 116, 101, 120, 116, 32, 115, 116, 114, 105, 110, 103, 33};
        String expected = "U29tZSB0ZXh0IHN0cmluZyE=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToAltBase64_EmptyArray() {
        byte[] input = {};
        String expected = "";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64_TypicalCase() {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        String expected = "SGVsbG8gV29ybGQh";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64_EmptyArray() {
        byte[] input = {};
        String expected = "";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }
}