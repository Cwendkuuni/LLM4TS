import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    // Test cases for altBase64ToByteArray method

    @Test
    public void testAltBase64ToByteArray_WithValidInput_ReturnsByteArray() {
        String input = "YWJj";
        byte[] expected = {97, 98, 99};
        byte[] actual = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_WithInvalidInput_ThrowsIllegalArgumentException() {
        String input = "abc";
        Base64.altBase64ToByteArray(input);
    }

    // Test cases for base64ToByteArray method

    @Test
    public void testBase64ToByteArray_WithValidInput_ReturnsByteArray() {
        String input = "YWJj";
        byte[] expected = {97, 98, 99};
        byte[] actual = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_WithInvalidInput_ThrowsIllegalArgumentException() {
        String input = "abc";
        Base64.base64ToByteArray(input);
    }

    // Test cases for byteArrayToAltBase64 method

    @Test
    public void testByteArrayToAltBase64_WithValidInput_ReturnsBase64String() {
        byte[] input = {97, 98, 99};
        String expected = "YWJj";
        String actual = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, actual);
    }

    // Test cases for byteArrayToBase64 method

    @Test
    public void testByteArrayToBase64_WithValidInput_ReturnsBase64String() {
        byte[] input = {97, 98, 99};
        String expected = "YWJj";
        String actual = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, actual);
    }

    // Additional test cases

    @Test
    public void testMain_WithValidInput_ReturnsExpectedOutput() {
        String expected = "MDEyMzQ1Njc4OQ==";
        Base64.main(new String[0]);
        String actual = System.out.toString().trim();
        Assert.assertEquals(expected, actual);
    }

}