import org.junit.Assert;
import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Basic test case
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, output);

        // Empty string
        input = "";
        expected = new byte[0];
        output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, output);

        // String with padding
        input = "SGVsbG8gd29ybGQ==";
        expected = "Hello world".getBytes();
        output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, output);

        // String with invalid length
        input = "SGVsbG8gd29ybG";
        try {
            output = Base64.altBase64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Basic test case
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] output = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, output);

        // Empty string
        input = "";
        expected = new byte[0];
        output = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, output);

        // String with padding
        input = "SGVsbG8gd29ybGQ==";
        expected = "Hello world".getBytes();
        output = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, output);

        // String with invalid length
        input = "SGVsbG8gd29ybG";
        try {
            output = Base64.base64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Basic test case
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, output);

        // Empty byte array
        input = new byte[0];
        expected = "";
        output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, output);
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Basic test case
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, output);

        // Empty byte array
        input = new byte[0];
        expected = "";
        output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, output);
    }
    
}