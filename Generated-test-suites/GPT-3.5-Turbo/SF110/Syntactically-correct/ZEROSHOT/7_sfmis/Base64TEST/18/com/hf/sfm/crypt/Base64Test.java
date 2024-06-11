import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Test case 1
        String s1 = "SGVsbG8gd29ybGQ=";
        byte[] result1 = Base64.altBase64ToByteArray(s1);
        byte[] expected1 = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        assertArrayEquals(expected1, result1);

        // Test case 2
        String s2 = "dGVzdCBtZXNz";
        byte[] result2 = Base64.altBase64ToByteArray(s2);
        byte[] expected2 = { 116, 101, 115, 116, 32, 109, 101, 115, 115 };
        assertArrayEquals(expected2, result2);
    }

    @Test
    public void testBase64ToByteArray() {
        // Test case 1
        String s1 = "SGVsbG8gd29ybGQ=";
        byte[] result1 = Base64.base64ToByteArray(s1);
        byte[] expected1 = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        assertArrayEquals(expected1, result1);

        // Test case 2
        String s2 = "dGVzdCBtZXNz";
        byte[] result2 = Base64.base64ToByteArray(s2);
        byte[] expected2 = { 116, 101, 115, 116, 32, 109, 101, 115, 115 };
        assertArrayEquals(expected2, result2);
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Test case 1
        byte[] bb1 = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        String result1 = Base64.byteArrayToAltBase64(bb1);
        String expected1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expected1, result1);

        // Test case 2
        byte[] bb2 = { 116, 101, 115, 116, 32, 109, 101, 115, 115 };
        String result2 = Base64.byteArrayToAltBase64(bb2);
        String expected2 = "dGVzdCBtZXNz";
        assertEquals(expected2, result2);
    }

    @Test
    public void testByteArrayToBase64() {
        // Test case 1
        byte[] bb1 = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        String result1 = Base64.byteArrayToBase64(bb1);
        String expected1 = "SGVsbG8gd29ybGQ=";
        assertEquals(expected1, result1);

        // Test case 2
        byte[] bb2 = { 116, 101, 115, 116, 32, 109, 101, 115, 115 };
        String result2 = Base64.byteArrayToBase64(bb2);
        String expected2 = "dGVzdCBtZXNz";
        assertEquals(expected2, result2);
    }
}