import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_typical() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArray_edge() {
        String s = "";
        byte[] expected = {};
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_error() {
        String s = "SGVsbG8gd29ybGQ==";
        Base64.altBase64ToByteArray(s);
    }

    @Test
    public void testBase64ToByteArray_typical() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray_edge() {
        String s = "";
        byte[] expected = {};
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_error() {
        String s = "SGVsbG8gd29ybGQ==";
        Base64.base64ToByteArray(s);
    }

    @Test
    public void testByteArrayToAltBase64_typical() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_edge() {
        byte[] bb = {};
        String expected = "";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_typical() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_edge() {
        byte[] bb = {};
        String expected = "";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
    }

}