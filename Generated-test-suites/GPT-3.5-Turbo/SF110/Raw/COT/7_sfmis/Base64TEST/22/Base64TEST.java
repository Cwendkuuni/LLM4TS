import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {
  
    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ="; // "Hello world" in base64
        byte[] expectedOutput = "Hello world".getBytes();
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ="; // "Hello world" in base64
        byte[] expectedOutput = "Hello world".getBytes();
        byte[] actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = "Hello world".getBytes();
        String expectedOutput = "SGVsbG8gd29ybGQ="; // "Hello world" in base64
        String actualOutput = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] input = "Hello world".getBytes();
        String expectedOutput = "SGVsbG8gd29ybGQ="; // "Hello world" in base64
        String actualOutput = Base64.byteArrayToBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testAltBase64ToByteArrayEdgeCases() {
        String input = "";
        byte[] expectedOutput = new byte[0];
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
        
        input = "==";
        expectedOutput = new byte[0];
        actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
        
        input = "A=";
        expectedOutput = new byte[] { 0 };
        actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
        
        input = "AB==";
        expectedOutput = new byte[] { 0, 1 };
        actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testBase64ToByteArrayEdgeCases() {
        String input = "";
        byte[] expectedOutput = new byte[0];
        byte[] actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
        
        input = "==";
        expectedOutput = new byte[0];
        actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
        
        input = "A=";
        expectedOutput = new byte[] { 0 };
        actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
        
        input = "AB==";
        expectedOutput = new byte[] { 0, 1 };
        actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayException() {
        String input = "SGVsbG8gd29ybG=="; // Invalid base64 string
        Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayException() {
        String input = "SGVsbG8gd29ybG=="; // Invalid base64 string
        Base64.base64ToByteArray(input);
    }
  
}