import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = new byte[] { 72, 101, 108, 108, 111 };
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }
    
    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = new byte[] { 72, 101, 108, 108, 111 };
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[] { 72, 101, 108, 108, 111 };
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[] { 72, 101, 108, 108, 111 };
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }
}