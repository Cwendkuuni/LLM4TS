import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] result = Base64.altBase64ToByteArray("SGVsbG8gd29ybGQ=");
        byte[] expected = "Hello world".getBytes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] result = Base64.base64ToByteArray("SGVsbG8gd29ybGQ=");
        byte[] expected = "Hello world".getBytes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64() {
        String result = Base64.byteArrayToAltBase64("Hello world".getBytes());
        String expected = "SGVsbG8gd29ybGQ=";
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        String result = Base64.byteArrayToBase64("Hello world".getBytes());
        String expected = "SGVsbG8gd29ybGQ=";
        assertEquals(expected, result);
    }
}