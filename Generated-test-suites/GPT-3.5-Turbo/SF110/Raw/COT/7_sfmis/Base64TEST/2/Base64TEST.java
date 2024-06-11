
import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedResult = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actualResult = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedResult = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actualResult = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expectedResult = "SGVsbG8gd29ybGQ=";
        String actualResult = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expectedResult = "SGVsbG8gd29ybGQ=";
        String actualResult = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        String input = "SGVsbG8gd29ybGQ";
        byte[] actualResult = Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        String input = "SGVsbG8gd29ybGQ";
        byte[] actualResult = Base64.base64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidCharacter() {
        String input = "SGV!sG8gd29ybGQ=";
        byte[] actualResult = Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidCharacter() {
        String input = "SGV!sG8gd29ybGQ=";
        byte[] actualResult = Base64.base64ToByteArray(input);
    }

    @Test
    public void testMain() {
        // This test is for code coverage
        String[] args = {};
        Base64.main(args);
    }
}
