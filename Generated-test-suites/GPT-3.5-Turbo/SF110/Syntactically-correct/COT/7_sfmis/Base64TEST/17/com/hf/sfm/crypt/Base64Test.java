
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQh"; // "Hello world!"
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQh"; // "Hello world!"
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33}; // "Hello world!"
        String expected = "SGVsbG8gd29ybGQh";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33}; // "Hello world!"
        String expected = "SGVsbG8gd29ybGQh";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
    }
}

