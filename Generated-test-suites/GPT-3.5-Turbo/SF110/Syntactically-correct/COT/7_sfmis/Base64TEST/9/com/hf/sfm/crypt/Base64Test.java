
import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Basic test case
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expected = "Hello World".getBytes();
        byte[] result = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: empty string
        input = "";
        expected = new byte[0];
        result = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: valid input with padding characters
        input = "SGVsbG8g";
        expected = "Hello".getBytes();
        result = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: invalid input with non-base64 characters
        input = "SGVsbG8g!!";
        try {
            result = Base64.altBase64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Basic test case
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expected = "Hello World".getBytes();
        byte[] result = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: empty string
        input = "";
        expected = new byte[0];
        result = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: valid input with padding characters
        input = "SGVsbG8g";
        expected = "Hello".getBytes();
        result = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: invalid input with non-base64 characters
        input = "SGVsbG8g!!";
        try {
            result = Base64.base64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Basic test case
        byte[] input = "Hello World".getBytes();
        String expected = "SGVsbG8gV29ybGQ=";
        String result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);

        // Edge case: empty byte array
        input = new byte[0];
        expected = "";
        result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        // Basic test case
        byte[] input = "Hello World".getBytes();
        String expected = "SGVsbG8gV29ybGQ=";
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);

        // Edge case: empty byte array
        input = new byte[0];
        expected = "";
        result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);
    }
}
