import org.junit.Assert;
import org.junit.Test;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray_emptyString() {
        String s = "";
        byte[] result = Base64.altBase64ToByteArray(s);
        Assert.assertArrayEquals(new byte[0], result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_lengthNotDivisibleBy4() {
        String s = "ABC";
        Base64.altBase64ToByteArray(s);
    }

    @Test
    public void testAltBase64ToByteArray_correctFormat() {
        String s = "QUJDRA==";
        byte[] result = Base64.altBase64ToByteArray(s);
        byte[] expected = {65, 66, 67, 68};
        Assert.assertArrayEquals(expected, result);
    }

    // Similarly write tests for other methods

    // Sample test cases for base64ToByteArray
    @Test
    public void testBase64ToByteArray_emptyString() {
        String s = "";
        byte[] result = Base64.base64ToByteArray(s);
        Assert.assertArrayEquals(new byte[0], result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_lengthNotDivisibleBy4() {
        String s = "ABC";
        Base64.base64ToByteArray(s);
    }

    @Test
    public void testBase64ToByteArray_correctFormat() {
        String s = "QUJDRA==";
        byte[] result = Base64.base64ToByteArray(s);
        byte[] expected = {65, 66, 67, 68};
        Assert.assertArrayEquals(expected, result);
    }

    // Sample test cases for byteArrayToAltBase64
    @Test
    public void testByteArrayToAltBase64_emptyArray() {
        byte[] bb = new byte[0];
        String result = Base64.byteArrayToAltBase64(bb);
        Assert.assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayToAltBase64_lengthNotDivisibleBy3() {
        byte[] bb = {1, 2};
        Base64.byteArrayToAltBase64(bb);
    }

    @Test
    public void testByteArrayToAltBase64_correctFormat() {
        byte[] bb = {65, 66, 67, 68};
        String result = Base64.byteArrayToAltBase64(bb);
        Assert.assertEquals("QUJDRA==", result);
    }

    // Sample test cases for byteArrayToBase64
    @Test
    public void testByteArrayToBase64_emptyArray() {
        byte[] bb = new byte[0];
        String result = Base64.byteArrayToBase64(bb);
        Assert.assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayToBase64_lengthNotDivisibleBy3() {
        byte[] bb = {1, 2};
        Base64.byteArrayToBase64(bb);
    }

    @Test
    public void testByteArrayToBase64_correctFormat() {
        byte[] bb = {65, 66, 67, 68};
        String result = Base64.byteArrayToBase64(bb);
        Assert.assertEquals("QUJDRA==", result);
    }
}