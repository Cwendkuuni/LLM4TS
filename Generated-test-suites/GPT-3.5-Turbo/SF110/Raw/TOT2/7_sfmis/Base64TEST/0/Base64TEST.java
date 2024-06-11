import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        byte[] result1 = Base64.altBase64ToByteArray("YW55IGNhcm5hbCBwbGVhc3VyZS4=");
        assertEquals("any carnal pleasure.", new String(result1));

        // Edge case - empty string
        byte[] result2 = Base64.altBase64ToByteArray("");
        assertEquals(0, result2.length);

        // Exception handling - illegal character
        try {
            Base64.altBase64ToByteArray("YW55IGNhcm5hbCBwbGVhc3VyZS!=)");
            fail("IllegalArgumentException expected for illegal character");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        byte[] result1 = Base64.base64ToByteArray("QUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVo=");
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", new String(result1));

        // Edge case - empty string
        byte[] result2 = Base64.base64ToByteArray("");
        assertEquals(0, result2.length);

        // Exception handling - string length not multiple of four
        try {
            Base64.base64ToByteArray("YW55IGNhcm5hbCBwbGVhc3VyZS");
            fail("IllegalArgumentException expected for string length not multiple of four");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        String result1 = Base64.byteArrayToAltBase64("Java is fun".getBytes());
        assertEquals("SmF2YSBpcyBmdW4=", result1);

        // Edge case - empty byte array
        String result2 = Base64.byteArrayToAltBase64(new byte[0]);
        assertEquals("", result2);
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        String result1 = Base64.byteArrayToBase64("This is a test".getBytes());
        assertEquals("VGhpcyBpcyBhIHRlc3Q=", result1);

        // Edge case - empty byte array
        String result2 = Base64.byteArrayToBase64(new byte[0]);
        assertEquals("", result2);
    }

    @Test
    public void testMain() {
        // This method is only for demonstration purposes, testing not required
    }
}