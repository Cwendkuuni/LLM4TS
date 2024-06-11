import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    // Test cases for altBase64ToByteArray() method

    @Test
    public void testAltBase64ToByteArray_withValidString() {
        String input = "aGVsbG8gd29ybGQ="; // base64 encoded "hello world"
        byte[] expected = { 104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // ASCII values of "hello world"
        byte[] result = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_withInvalidString() {
        String input = "aGVsbG8=d29ybGQ"; // base64 encoded "hello world" with one character missing
        Base64.altBase64ToByteArray(input); // should throw IllegalArgumentException
    }

    // Test cases for base64ToByteArray() method

    @Test
    public void testBase64ToByteArray_withValidString() {
        String input = "aGVsbG8gd29ybGQ="; // base64 encoded "hello world"
        byte[] expected = { 104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // ASCII values of "hello world"
        byte[] result = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_withInvalidString() {
        String input = "aGVsbG8=d29ybGQ"; // base64 encoded "hello world" with one character missing
        Base64.base64ToByteArray(input); // should throw IllegalArgumentException
    }

    // Test cases for byteArrayToAltBase64() method

    @Test
    public void testByteArrayToAltBase64_withValidByteArray() {
        byte[] input = { 104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // ASCII values of "hello world"
        String expected = "aGVsbG8gd29ybGQ="; // expected base64 encoded "hello world"
        String result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);
    }

    // Test cases for byteArrayToBase64() method

    @Test
    public void testByteArrayToBase64_withValidByteArray() {
        byte[] input = { 104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // ASCII values of "hello world"
        String expected = "aGVsbG8gd29ybGQ="; // expected base64 encoded "hello world"
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_withEmptyArray() {
        byte[] input = new byte[0]; // empty byte array
        String expected = "";
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);
    }

    // Additional Test cases for other methods can also be added here

}