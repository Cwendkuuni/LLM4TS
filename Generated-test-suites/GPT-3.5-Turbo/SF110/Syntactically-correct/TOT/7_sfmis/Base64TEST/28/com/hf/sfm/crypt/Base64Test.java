import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_typicalCase() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        
        byte[] output = Base64.altBase64ToByteArray(input);
        
        assertArrayEquals(expected, output);
    }
    
    @Test
    public void testAltBase64ToByteArray_emptyString() {
        String input = "";
        byte[] expected = new byte[0];
        
        byte[] output = Base64.altBase64ToByteArray(input);
        
        assertArrayEquals(expected, output);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidStringLength() {
        String input = "SGVsbG8gd2";
        
        Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidCharacter() {
        String input = "SGVsb&G8gd29ybGQ=";
        
        Base64.altBase64ToByteArray(input);
    }
    
    @Test
    public void testBase64ToByteArray_typicalCase() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        
        byte[] output = Base64.base64ToByteArray(input);
        
        assertArrayEquals(expected, output);
    }
    
    @Test
    public void testBase64ToByteArray_emptyString() {
        String input = "";
        byte[] expected = new byte[0];
        
        byte[] output = Base64.base64ToByteArray(input);
        
        assertArrayEquals(expected, output);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidStringLength() {
        String input = "SGVsbG8gd2";
        
        Base64.base64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidCharacter() {
        String input = "SGVsb&G8gd29ybGQ=";
        
        Base64.base64ToByteArray(input);
    }
    
    @Test
    public void testByteArrayToAltBase64_typicalCase() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        
        String output = Base64.byteArrayToAltBase64(input);
        
        assertEquals(expected, output);
    }
    
    @Test
    public void testByteArrayToAltBase64_emptyArray() {
        byte[] input = new byte[0];
        String expected = "";
        
        String output = Base64.byteArrayToAltBase64(input);
        
        assertEquals(expected, output);
    }
    
    @Test
    public void testByteArrayToBase64_typicalCase() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        
        String output = Base64.byteArrayToBase64(input);
        
        assertEquals(expected, output);
    }
    
    @Test
    public void testByteArrayToBase64_emptyArray() {
        byte[] input = new byte[0];
        String expected = "";
        
        String output = Base64.byteArrayToBase64(input);
        
        assertEquals(expected, output);
    }
}