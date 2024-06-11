import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // typical use case
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // edge case
        input = "SGVsbG8";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // error scenario (exception handling)
        input = "invalidBase64";
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.altBase64ToByteArray(input);
        });
    }

    @Test
    public void testBase64ToByteArray() {
        // typical use case
        String input = "U29ycnksIEkgYmFzZWQgYXNzdW1l";
        byte[] expected = {83, 111, 114, 114, 121, 44, 32, 73, 32, 98, 97, 115, 101, 100, 32, 97, 115, 115, 117, 109, 101};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        
        // edge case
        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        
        // error scenario (exception handling)
        input = "invalidBase64";
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.base64ToByteArray(input);
        });
}

    @Test
    public void testByteArrayToAltBase64() {
        // typical use case
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
        
        // edge case
        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        // typical use case
        byte[] input = {83, 111, 114, 114, 121, 44, 32, 73, 32, 98, 97, 115, 101, 100, 32, 97, 115, 115, 117, 109, 101};
        String expected = "U29ycnksIEkgYmFzZWQgYXNzdW1l";
        assertEquals(expected, Base64.byteArrayToBase64(input));
        
        // edge case
        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }

}