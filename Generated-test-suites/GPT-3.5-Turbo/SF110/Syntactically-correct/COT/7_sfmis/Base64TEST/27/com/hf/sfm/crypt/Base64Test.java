
import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] result = Base64.altBase64ToByteArray(input);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] result = Base64.base64ToByteArray(input);
        
        assertArrayEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        String input = "SGVsbG8gd29ybG=";
        byte[] result = Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        String input = "SGVsbG8gd29ybG=";
        byte[] result = Base64.base64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidCharacter() {
        String input = "SGVsbG8gd-9ybGQ=";
        byte[] result = Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidCharacter() {
        String input = "SGVsbG8gd-9ybGQ=";
        byte[] result = Base64.base64ToByteArray(input);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToAltBase64(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToBase64(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testMain() {
        // Not testing main method since it only outputs to console
    }
}
