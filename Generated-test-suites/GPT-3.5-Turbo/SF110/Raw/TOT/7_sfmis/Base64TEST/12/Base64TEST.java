import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);

        // Edge case: empty string
        s = "";
        expected = new byte[0];
        result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);

        // Error scenario: invalid characters
        s = "SGVsbG8gd29ybGQ!";
        try {
            Base64.altBase64ToByteArray(s);
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal character !", e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);

        // Edge case: empty string
        s = "";
        expected = new byte[0];
        result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);

        // Error scenario: string length not multiple of four
        s = "SGVsbG8gd29ybG";
        try {
            Base64.base64ToByteArray(s);
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] bb = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);

        // Edge case: empty byte array
        bb = new byte[0];
        expected = "";
        result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] bb = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);

        // Edge case: empty byte array
        bb = new byte[0];
        expected = "";
        result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
    }
}