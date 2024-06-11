import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQh";
        byte[] expectedByteArray = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        byte[] actualByteArray = Base64.altBase64ToByteArray(s);
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        String s = "SGVsbG8gd29ybGQh=";
        Base64.altBase64ToByteArray(s);
    }

    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQh";
        byte[] expectedByteArray = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        byte[] actualByteArray = Base64.base64ToByteArray(s);
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        String s = "SGVsbG8gd29ybGQh=";
        Base64.base64ToByteArray(s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidCharacter() {
        String s = "SGVsbG8gd29ybGQh&";
        Base64.base64ToByteArray(s);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] b = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        String expectedString = "SGVsbG8gd29ybGQh";
        String actualString = Base64.byteArrayToAltBase64(b);
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] b = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        String expectedString = "SGVsbG8gd29ybGQh";
        String actualString = Base64.byteArrayToBase64(b);
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testMain() {
        // TODO: Implement test case for main method
    }
}