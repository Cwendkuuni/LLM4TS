import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String input1 = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput1 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        Assert.assertArrayEquals(expectedOutput1, Base64.altBase64ToByteArray(input1));

        // Edge case: Empty string
        String input2 = "";
        byte[] expectedOutput2 = {};
        Assert.assertArrayEquals(expectedOutput2, Base64.altBase64ToByteArray(input2));

        // Error scenario: Invalid length
        String input3 = "SGVsbG8=";
        try {
            Base64.altBase64ToByteArray(input3);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        }
        catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        String input1 = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput1 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        Assert.assertArrayEquals(expectedOutput1, Base64.base64ToByteArray(input1));

        // Edge case: Empty string
        String input2 = "";
        byte[] expectedOutput2 = {};
        Assert.assertArrayEquals(expectedOutput2, Base64.base64ToByteArray(input2));

        // Error scenario: Invalid length
        String input3 = "SGVsbG8=";
        try {
            Base64.base64ToByteArray(input3);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        }
        catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input1 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expectedOutput1 = "SGVsbG8gd29ybGQ=";
        Assert.assertEquals(expectedOutput1, Base64.byteArrayToAltBase64(input1));

        // Edge case: Empty array
        byte[] input2 = {};
        String expectedOutput2 = "";
        Assert.assertEquals(expectedOutput2, Base64.byteArrayToAltBase64(input2));
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input1 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expectedOutput1 = "SGVsbG8gd29ybGQ=";
        Assert.assertEquals(expectedOutput1, Base64.byteArrayToBase64(input1));

        // Edge case: Empty array
        byte[] input2 = {};
        String expectedOutput2 = "";
        Assert.assertEquals(expectedOutput2, Base64.byteArrayToBase64(input2));
    }

    @Test
    public void testMain() {
        // Not testing main method as it only contains sample code for demonstration purposes
    }
}