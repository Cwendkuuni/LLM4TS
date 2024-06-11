import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ="; // Hello world in base64
        byte[] expectedOutput = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        assertArrayEquals(expectedOutput, Base64.altBase64ToByteArray(input));
    }
    
    @Test
    public void testAltBase64ToByteArray_EmptyString() {
        String input = "";
        byte[] expectedOutput = {};
        assertArrayEquals(expectedOutput, Base64.altBase64ToByteArray(input));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_InvalidLength() {
        String input = "SGVsbG8gd29ybGQ=="; // Invalid length should throw exception
        Base64.altBase64ToByteArray(input);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ="; // Hello world in base64
        byte[] expectedOutput = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        assertArrayEquals(expectedOutput, Base64.base64ToByteArray(input));
    }
    
    @Test
    public void testBase64ToByteArray_EmptyString() {
        String input = "";
        byte[] expectedOutput = {};
        assertArrayEquals(expectedOutput, Base64.base64ToByteArray(input));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_InvalidLength() {
        String input = "SGVsbG8gd29ybGQ=="; // Invalid length should throw exception
        Base64.base64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_InvalidCharacter() {
        String input = "SGVsbG8gd29ybGQ!"; // Invalid character should throw exception
        Base64.base64ToByteArray(input);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // Hello world
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        assertEquals(expectedOutput, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToAltBase64_EmptyArray() {
        byte[] input = {};
        String expectedOutput = "";
        assertEquals(expectedOutput, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // Hello world
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        assertEquals(expectedOutput, Base64.byteArrayToBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64_EmptyArray() {
        byte[] input = {};
        String expectedOutput = "";
        assertEquals(expectedOutput, Base64.byteArrayToBase64(input));
    }

}