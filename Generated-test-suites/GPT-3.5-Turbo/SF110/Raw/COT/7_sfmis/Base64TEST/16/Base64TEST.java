import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        byte[] actual = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        byte[] actual = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        String actual = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        String actual = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidInput() {
        String input = "SGVsbG8";
        Base64.altBase64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidInput2() {
        String input = "SGVsbG8==";
        Base64.altBase64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidInput() {
        String input = "SGVsbG8";
        Base64.base64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidInput2() {
        String input = "SGVsbG8==";
        Base64.base64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayNullInput() {
        String input = null;
        Base64.altBase64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayNullInput() {
        String input = null;
        Base64.base64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayToAltBase64NullInput() {
        byte[] input = null;
        Base64.byteArrayToAltBase64(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayToBase64NullInput() {
        byte[] input = null;
        Base64.byteArrayToBase64(input);
    }

}