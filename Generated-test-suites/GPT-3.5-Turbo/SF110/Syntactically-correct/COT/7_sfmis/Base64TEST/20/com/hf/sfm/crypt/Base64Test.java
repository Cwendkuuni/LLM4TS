
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        String base64String = "SGVsbG8gd29ybGQ=";
        byte[] expected = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        byte[] actual = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String base64String = "SGVsbG8gd29ybGQ=";
        byte[] expected = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        byte[] actual = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] byteArray = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] byteArray = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToBase64(byteArray);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        String base64String = "SGVsbG8gd29ybG"; // Invalid length, not a multiple of four
        Base64.altBase64ToByteArray(base64String);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        String base64String = "SGVsbG8gd29ybG"; // Invalid length, not a multiple of four
        Base64.base64ToByteArray(base64String);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayIllegalCharacter() {
        String base64String = "SGVsb!G8gd29ybGQ="; // Illegal character '!'
        Base64.base64ToByteArray(base64String);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayIllegalCharacter2() {
        String base64String = "SGVsb=G8gd29ybGQ="; // Illegal character '='
        Base64.base64ToByteArray(base64String);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayIllegalCharacter3() {
        String base64String = "SGVsbG8gd29ybGQ"; // Missing padding character '='
        Base64.base64ToByteArray(base64String);
    }
}
