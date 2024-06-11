import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = "Hello world".getBytes();
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = "Hello world".getBytes();
        byte[] actualOutput = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = "Hello world".getBytes();
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        String actualOutput = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = "Hello world".getBytes();
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        String actualOutput = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
}