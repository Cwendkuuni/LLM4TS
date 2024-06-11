
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        
        byte[] result = Base64.altBase64ToByteArray(s);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        
        byte[] result = Base64.base64ToByteArray(s);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] b = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        
        String result = Base64.byteArrayToAltBase64(b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] b = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        
        String result = Base64.byteArrayToBase64(b);
        
        assertEquals(expected, result);
    }
    
    // Additional test case for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayException() {
        String s = "SGVsbG8=="; // string length is not a multiple of four
        
        Base64.altBase64ToByteArray(s);
    }
    
    // Additional test case for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayException() {
        String s = "SGVsbG8=="; // string length is not a multiple of four
        
        Base64.base64ToByteArray(s);
    }
}
