import org.junit.*;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUGBwgJ");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUGBwgJ");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testByteArrayToAltBase64() {
        String expected = "AQIDBAUGBwgJ";
        byte[] input = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        String expected = "AQIDBAUGBwgJ";
        byte[] input = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }
}