import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray_typicalCase() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testAltBase64ToByteArray_emptyString() {
        String s = "";
        byte[] expected = {};
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testAltBase64ToByteArray_invalidLength() {
        String s = "SGVsbG8gd29ybGQ";
        try {
            byte[] result = Base64.altBase64ToByteArray(s);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught successfully
        }
    }
    
    @Test
    public void testBase64ToByteArray_typicalCase() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testBase64ToByteArray_emptyString() {
        String s = "";
        byte[] expected = {};
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testBase64ToByteArray_invalidLength() {
        String s = "SGVsbG8gd29ybGQ";
        try {
            byte[] result = Base64.base64ToByteArray(s);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught successfully
        }
    }
    
    @Test
    public void testByteArrayToAltBase64_typicalCase() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToAltBase64_emptyArray() {
        byte[] bb = {};
        String expected = "";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToBase64_typicalCase() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToBase64_emptyArray() {
        byte[] bb = {};
        String expected = "";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
    }
    
    @Test
    public void testMain_typicalCase() {
        String[] args = {};
        Base64.main(args); // Just checking if no exception is thrown
    }
}