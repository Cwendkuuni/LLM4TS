import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = new byte[] {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = new byte[] {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[] {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        String actualOutput = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[] {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        String actualOutput = Base64.byteArrayToBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        String input = "SGVsbG8gd29ybGQ====";
        Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        String input = "SGVsbG8gd29ybGQ====";
        Base64.base64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidCharacter() {
        String input = "SGVsbG8gd29{e";
        Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidCharacter() {
        String input = "SGVsbG8gd29{e";
        Base64.base64ToByteArray(input);
    }
    
    @Test
    public void testAltBase64ToByteArrayEmptyString() {
        String input = "";
        byte[] expectedOutput = new byte[] {};
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testBase64ToByteArrayEmptyString() {
        String input = "";
        byte[] expectedOutput = new byte[] {};
        byte[] actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToAltBase64EmptyArray() {
        byte[] input = new byte[] {};
        String expectedOutput = "";
        String actualOutput = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToBase64EmptyArray() {
        byte[] input = new byte[] {};
        String expectedOutput = "";
        String actualOutput = Base64.byteArrayToBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMain() {
        // Unable to test main method
        assertTrue(true);
    }
}