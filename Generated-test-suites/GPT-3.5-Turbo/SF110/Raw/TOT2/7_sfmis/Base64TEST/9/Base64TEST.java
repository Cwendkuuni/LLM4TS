import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testExceptionHandling() {
        try {
            Base64._$23180("InvalidInput", true);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}