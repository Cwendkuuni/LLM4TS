import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));

        // Edge case
        input = "";
        assertArrayEquals(new byte[0], Base64.altBase64ToByteArray(input));

        // Error scenario
        try {
            Base64.altBase64ToByteArray("SGVsbG8=="); // invalid input with extra padding
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));

        // Edge case
        input = "";
        assertArrayEquals(new byte[0], Base64.base64ToByteArray(input));

        // Error scenario
        try {
            Base64.base64ToByteArray("SGVsbG8=="); // invalid input with extra padding
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));

        // Edge case
        input = new byte[0];
        assertEquals("", Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));

        // Edge case
        input = new byte[0];
        assertEquals("", Base64.byteArrayToBase64(input));
    }

    @Test
    public void test_$23183() {
        // Typical use case
        char c = 'A';
        byte[] bb = Base64._$23169;
        assertEquals(-1, Base64._$23183(c, bb));

        // Error scenario
        try {
            c = '%'; // illegal character
            Base64._$23183(c, bb);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}