import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(s));
    }
    
    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.base64ToByteArray(s));
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] b = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        assertEquals(expected, Base64.byteArrayToAltBase64(b));
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] b = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        assertEquals(expected, Base64.byteArrayToBase64(b));
    }
}