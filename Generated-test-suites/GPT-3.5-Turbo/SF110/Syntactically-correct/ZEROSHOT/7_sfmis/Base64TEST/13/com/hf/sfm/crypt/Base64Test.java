import org.junit.*;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] result = Base64.altBase64ToByteArray("SGVsbG8gd29ybGQ=");
        byte[] expected = "Hello world".getBytes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArray_EmptyString() {
        byte[] result = Base64.altBase64ToByteArray("");
        byte[] expected = new byte[0];
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_InvalidLength() {
        Base64.altBase64ToByteArray("SGVsbG8gd29ybGQ==");
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] result = Base64.base64ToByteArray("SGVsbG8gd29ybGQ=");
        byte[] expected = "Hello world".getBytes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray_EmptyString() {
        byte[] result = Base64.base64ToByteArray("");
        byte[] expected = new byte[0];
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_InvalidLength() {
        Base64.base64ToByteArray("SGVsbG8gd29ybGQ==");
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = "Hello world".getBytes();
        String result = Base64.byteArrayToAltBase64(bb);
        String expected = "SGVsbG8gd29ybGQ=";
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_EmptyArray() {
        byte[] bb = new byte[0];
        String result = Base64.byteArrayToAltBase64(bb);
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] bb = "Hello world".getBytes();
        String result = Base64.byteArrayToBase64(bb);
        String expected = "SGVsbG8gd29ybGQ=";
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_EmptyArray() {
        byte[] bb = new byte[0];
        String result = Base64.byteArrayToBase64(bb);
        String expected = "";
        assertEquals(expected, result);
    }
}