import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_typicalCase() {
        String input = "QmFzZTY0IGVuY29kaW5n";
        byte[] expected = {66, 97, 115, 101, 54, 52, 32, 101, 110, 99, 111, 100, 105, 110, 103};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }
    
    @Test
    public void testAltBase64ToByteArray_emptyString() {
        String input = "";
        byte[] expected = {};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidStringLength() {
        String input = "QmFzZTY0IGVuY29kaW5";
        Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidCharacter() {
        String input = "QmF#ZTY0IGNkaW5n";
        Base64.altBase64ToByteArray(input);
    }

    @Test
    public void testBase64ToByteArray_typicalCase() {
        String input = "VGhpcyBpcyBhIG5lZWQgdG8gdGVzdCBjcmVhdGU=";
        byte[] expected = {84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 110, 101, 101, 100, 32, 116, 111, 32, 116, 101, 115, 116, 32, 99, 114, 101, 97, 116, 101};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }
    
    @Test
    public void testBase64ToByteArray_emptyString() {
        String input = "";
        byte[] expected = {};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidStringLength() {
        String input = "VGhpcyBpcyBhIG5lZWQgdG8gdGVzdCBjcmVh=";
        Base64.base64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidCharacter() {
        String input = "VG(hpcyBpcyBhIG5lZWQgdG8gdGVzdCBjcmVh";
        Base64.base64ToByteArray(input);
    }

    @Test
    public void testByteArrayToAltBase64_typicalCase() {
        byte[] input = {84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 110, 101, 101, 100, 32, 116, 111, 32, 116, 101, 115, 116, 32, 99, 114, 101, 97, 116, 101};
        String expected = "VGhpcyBpcyBhIG5lZWQgdG8gdGVzdCBjcmVhdGU=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToAltBase64_emptyArray() {
        byte[] input = {};
        String expected = "";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64_typicalCase() {
        byte[] input = {66, 97, 115, 101, 54, 52, 32, 101, 110, 99, 111, 100, 105, 110, 103};
        String expected = "QmFzZTY0IGVuY29kaW5n";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64_emptyArray() {
        byte[] input = {};
        String expected = "";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }
    
    @Test
    public void testMain() {
        // Since the main method only prints to console, there is no need to test it.
        assertTrue(true);
    }
}