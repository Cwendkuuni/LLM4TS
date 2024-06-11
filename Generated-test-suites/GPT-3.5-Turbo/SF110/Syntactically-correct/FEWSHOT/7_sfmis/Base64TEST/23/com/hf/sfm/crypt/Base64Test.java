import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));

        input = "SGVsbG8=";
        expected = "Hello".getBytes();
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));

        input = "AA==";
        expected = new byte[] { 0 };
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));

        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidInput() {
        String input = "SGVsbG8gdG8gZGVjb2Rl";
        Base64.altBase64ToByteArray(input);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        assertArrayEquals(expected, Base64.base64ToByteArray(input));

        input = "SGVsbG8=";
        expected = "Hello".getBytes();
        assertArrayEquals(expected, Base64.base64ToByteArray(input));

        input = "AA==";
        expected = new byte[] { 0 };
        assertArrayEquals(expected, Base64.base64ToByteArray(input));

        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidInput() {
        String input = "SGVsbG8gdG8gZGVjb2Rl";
        Base64.base64ToByteArray(input);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));

        input = "Hello".getBytes();
        expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));

        input = new byte[] { 0 };
        expected = "AA==";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));

        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        assertEquals(expected, Base64.byteArrayToBase64(input));

        input = "Hello".getBytes();
        expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));

        input = new byte[] { 0 };
        expected = "AA==";
        assertEquals(expected, Base64.byteArrayToBase64(input));

        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }

    @Test
    public void testMain() {
        // No assertion to check console output
        Base64.main(null);
    }
}