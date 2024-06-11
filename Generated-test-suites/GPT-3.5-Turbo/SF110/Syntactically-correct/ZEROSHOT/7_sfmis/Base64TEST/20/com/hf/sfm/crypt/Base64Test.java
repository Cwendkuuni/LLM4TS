import static org.junit.Assert.*;
import org.junit.Test;
import com.hf.sfm.crypt.Base64;

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

    @Test
    public void testByteArrayToAltBase64() {
        byte[] b = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToAltBase64(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] b = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToBase64(b);
        assertEquals(expected, actual);
    }
}