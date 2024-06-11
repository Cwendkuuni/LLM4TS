import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_success(){
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = "Hello world".getBytes();
        
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAltBase64ToByteArray_invalidString(){
        String input = "SGVsbG8gd29ybGQ"; // Invalid input without "=" padding.
        
        try {
            byte[] actualOutput = Base64.altBase64ToByteArray(input);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testAltBase64ToByteArray_emptyString(){
        String input = "";
        byte[] expectedOutput = new byte[0];
        
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testBase64ToByteArray_success(){
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = "Hello world".getBytes();
        
        byte[] actualOutput = Base64.base64ToByteArray(input);
        
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testBase64ToByteArray_invalidString(){
        String input = "SGVsbG8gd29ybGQ"; // Invalid input without "=" padding.
        
        try {
            byte[] actualOutput = Base64.base64ToByteArray(input);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testBase64ToByteArray_emptyString(){
        String input = "";
        byte[] expectedOutput = new byte[0];
        
        byte[] actualOutput = Base64.base64ToByteArray(input);
        
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToAltBase64_success(){
        byte[] input = "Hello world".getBytes();
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        
        String actualOutput = Base64.byteArrayToAltBase64(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToAltBase64_emptyArray(){
        byte[] input = new byte[0];
        String expectedOutput = "";
        
        String actualOutput = Base64.byteArrayToAltBase64(input);
        
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToBase64_success(){
        byte[] input = "Hello world".getBytes();
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        
        String actualOutput = Base64.byteArrayToBase64(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToBase64_emptyArray(){
        byte[] input = new byte[0];
        String expectedOutput = "";
        
        String actualOutput = Base64.byteArrayToBase64(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
}