
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidStringLength() {
        String s = "SGVsbG8gd29ybGQ";
        byte[] actual = Base64.altBase64ToByteArray(s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidStringLength() {
        String s = "SGVsbG8gd29ybGQ";
        byte[] actual = Base64.base64ToByteArray(s);
    }

    @Test
    public void testAltBase64ToByteArrayEmptyString() {
        String s = "";
        byte[] expected = new byte[0];
        byte[] actual = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArrayEmptyString() {
        String s = "";
        byte[] expected = new byte[0];
        byte[] actual = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] bb = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToBase64(bb);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        // Main method prints the base64 encoding and decoding of "0123456789"
        // Cannot assert the output, just ensuring no exceptions are thrown
        String[] args = {};
        Base64.main(args);
    }

}
