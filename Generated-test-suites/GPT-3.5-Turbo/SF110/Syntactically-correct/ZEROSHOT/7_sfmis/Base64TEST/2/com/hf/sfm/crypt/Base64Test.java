import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gV29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(s));
    }
    
    @Test
    public void testAltBase64ToByteArrayEmptyString() {
        String s = "";
        byte[] expected = new byte[0];
        assertArrayEquals(expected, Base64.altBase64ToByteArray(s));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        String s = "SGVsbG8gV29ybGQ";
        Base64.altBase64ToByteArray(s);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gV29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        assertArrayEquals(expected, Base64.base64ToByteArray(s));
    }
    
    @Test
    public void testBase64ToByteArrayEmptyString() {
        String s = "";
        byte[] expected = new byte[0];
        assertArrayEquals(expected, Base64.base64ToByteArray(s));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        String s = "SGVsbG8gV29ybGQ";
        Base64.base64ToByteArray(s);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expected = "SGVsbG8gV29ybGQ=";
        assertEquals(expected, Base64.byteArrayToAltBase64(bb));
    }
    
    @Test
    public void testByteArrayToAltBase64EmptyArray() {
        byte[] bb = new byte[0];
        String expected = "";
        assertEquals(expected, Base64.byteArrayToAltBase64(bb));
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expected = "SGVsbG8gV29ybGQ=";
        assertEquals(expected, Base64.byteArrayToBase64(bb));
    }
    
    @Test
    public void testByteArrayToBase64EmptyArray() {
        byte[] bb = new byte[0];
        String expected = "";
        assertEquals(expected, Base64.byteArrayToBase64(bb));
    }
}