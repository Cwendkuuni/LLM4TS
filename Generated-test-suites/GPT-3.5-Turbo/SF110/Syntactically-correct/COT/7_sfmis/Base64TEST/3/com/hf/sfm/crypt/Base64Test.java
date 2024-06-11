
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;

public class Base64Test {
    
    private static Base64 base64;
    
    @BeforeClass
    public static void setUp() {
        base64 = new Base64();
    }
    
    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actual = base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actual = base64.base64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = base64.byteArrayToAltBase64(bb);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = base64.byteArrayToBase64(bb);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidLength() {
        String s = "SGVsbG8gd29ybGQ";
        base64.altBase64ToByteArray(s);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithIllegalCharacter() {
        String s = "SGVsbG8gd29ybG!";
        base64.altBase64ToByteArray(s);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidLength() {
        String s = "SGVsbG8gd29ybGQ";
        base64.base64ToByteArray(s);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithIllegalCharacter() {
        String s = "SGVsbG8gd29ybG!";
        base64.base64ToByteArray(s);
    }
}

