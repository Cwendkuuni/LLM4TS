import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actual = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actual = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToBase64(bb);
        assertEquals(expected, actual);
    }
}