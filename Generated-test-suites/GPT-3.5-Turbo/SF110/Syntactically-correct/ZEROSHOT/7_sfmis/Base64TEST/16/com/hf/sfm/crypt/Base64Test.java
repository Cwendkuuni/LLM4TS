import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        byte[] result = Base64.altBase64ToByteArray(input);
        assertEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        byte[] result = Base64.base64ToByteArray(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expected = "SGVsbG8gV29ybGQ=";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expected = "SGVsbG8gV29ybGQ=";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }
}