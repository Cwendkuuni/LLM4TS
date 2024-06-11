import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQh";
        byte[] expected = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33 };
        byte[] actual = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testAltBase64ToByteArray_emptyString() {
        String s = "";
        byte[] expected = {};
        byte[] actual = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidStringLength() {
        String s = "SGVsbG8gd29ybGQh=";
        Base64.altBase64ToByteArray(s);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQh";
        byte[] expected = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33 };
        byte[] actual = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testBase64ToByteArray_emptyString() {
        String s = "";
        byte[] expected = {};
        byte[] actual = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidStringLength() {
        String s = "SGVsbG8gd29ybGQh=";
        Base64.base64ToByteArray(s);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidCharacter() {
        String s = "SGVsbG8gd29ybGQh!";
        Base64.base64ToByteArray(s);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33 };
        String expected = "SGVsbG8gd29ybGQh";
        String actual = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToAltBase64_emptyArray() {
        byte[] bb = {};
        String expected = "";
        String actual = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] bb = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33 };
        String expected = "SGVsbG8gd29ybGQh";
        String actual = Base64.byteArrayToBase64(bb);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToBase64_emptyArray() {
        byte[] bb = {};
        String expected = "";
        String actual = Base64.byteArrayToBase64(bb);
        assertEquals(expected, actual);
    }
}