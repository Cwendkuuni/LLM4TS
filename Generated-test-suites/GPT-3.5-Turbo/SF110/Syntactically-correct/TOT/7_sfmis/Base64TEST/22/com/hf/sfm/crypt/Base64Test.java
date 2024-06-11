import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray_oneChar() {
        String input = "AQ==";
        byte[] expected = {1};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }
    
    @Test
    public void testAltBase64ToByteArray_multipleChars() {
        String input = "SGVsbG8gd29ybGQh";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidLength() {
        String input = "SGVsbG8gd29ybGQ";
        Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidCharacter() {
        String input = "AQ==!";
        Base64.altBase64ToByteArray(input);
    }
    
    @Test
    public void testBase64ToByteArray_oneChar() {
        String input = "AQ==";
        byte[] expected = {1};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }
    
    // continue with more test cases for other methods...

}