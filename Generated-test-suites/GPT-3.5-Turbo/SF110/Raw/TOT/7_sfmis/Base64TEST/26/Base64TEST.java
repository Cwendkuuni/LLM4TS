
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case: string length is a multiple of four
        byte[] expected = {-87, -121, -51, -17};
        byte[] result = Base64.altBase64ToByteArray("BBbnz/X8");
        assertArrayEquals(expected, result);
        
        // Edge case: string length is 0
        expected = new byte[0];
        result = Base64.altBase64ToByteArray("");
        assertArrayEquals(expected, result);
        
        // Error scenario: string length is not a multiple of four
        try {
            result = Base64.altBase64ToByteArray("ABC");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Typical use case: string length is a multiple of four
        byte[] expected = {-87, -121, -51, -17};
        byte[] result = Base64.base64ToByteArray("BBbnz/X8");
        assertArrayEquals(expected, result);
        
        // Edge case: string length is 0
        expected = new byte[0];
        result = Base64.base64ToByteArray("");
        assertArrayEquals(expected, result);
        
        // Error scenario: string length is not a multiple of four
        try {
            result = Base64.base64ToByteArray("ABC");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case: byte array length is a multiple of three
        String expected = "BBbnz/X8";
        byte[] bb = {-87, -121, -51, -17};
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
        
        // Typical use case: byte array length is not a multiple of three
        expected = "BBZ";
        bb = new byte[] {-87, -121, -51};
        result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
        
        // Edge case: byte array length is 0
        expected = "";
        bb = new byte[0];
        result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Typical use case: byte array length is a multiple of three
        String expected = "BBbnz/X8";
        byte[] bb = {-87, -121, -51, -17};
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
        
        // Typical use case: byte array length is not a multiple of three
        expected = "BBZ=";
        bb = new byte[] {-87, -121, -51};
        result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
        
        // Edge case: byte array length is 0
        expected = "";
        bb = new byte[0];
        result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
    }
}
