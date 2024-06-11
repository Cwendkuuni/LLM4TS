import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Base64Test {

    @Before
    public void setUp() {
        // Set up necessary configurations or objects before each test case
    }

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case: convert valid altBase64 string to byte array
        String altBase64 = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.altBase64ToByteArray(altBase64);
        Assert.assertArrayEquals(expected, actual);
        
        // Edge case: convert empty altBase64 string to byte array
        altBase64 = "";
        expected = new byte[] {};
        actual = Base64.altBase64ToByteArray(altBase64);
        Assert.assertArrayEquals(expected, actual);
        
        // Error scenario: convert invalid altBase64 string with non-multiple of four length to byte array
        altBase64 = "SGV";
        try {
            Base64.altBase64ToByteArray(altBase64);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case: convert valid base64 string to byte array
        String base64 = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.base64ToByteArray(base64);
        Assert.assertArrayEquals(expected, actual);
        
        // Edge case: convert empty base64 string to byte array
        base64 = "";
        expected = new byte[] {};
        actual = Base64.base64ToByteArray(base64);
        Assert.assertArrayEquals(expected, actual);
        
        // Error scenario: convert invalid base64 string with non-multiple of four length to byte array
        base64 = "SGV";
        try {
            Base64.base64ToByteArray(base64);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case: convert valid byte array to altBase64 string
        byte[] byteArray = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToAltBase64(byteArray);
        Assert.assertEquals(expected, actual);
        
        // Edge case: convert empty byte array to altBase64 string
        byteArray = new byte[] {};
        expected = "";
        actual = Base64.byteArrayToAltBase64(byteArray);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case: convert valid byte array to base64 string
        byte[] byteArray = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToBase64(byteArray);
        Assert.assertEquals(expected, actual);
        
        // Edge case: convert empty byte array to base64 string
        byteArray = new byte[] {};
        expected = "";
        actual = Base64.byteArrayToBase64(byteArray);
        Assert.assertEquals(expected, actual);
    }
}