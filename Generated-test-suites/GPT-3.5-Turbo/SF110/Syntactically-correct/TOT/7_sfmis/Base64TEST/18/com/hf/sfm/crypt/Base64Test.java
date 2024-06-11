import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_typicalCase() {
        String input = "YWJj";
        byte[] expected = { 97, 98, 99 };
        byte[] actual = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testAltBase64ToByteArray_emptyString() {
        String input = "";
        byte[] expected = {};
        byte[] actual = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidLength() {
        String input = "YWJ";
        Base64.altBase64ToByteArray(input);
    }

    @Test
    public void testBase64ToByteArray_typicalCase() {
        String input = "YWJj";
        byte[] expected = { 97, 98, 99 };
        byte[] actual = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testBase64ToByteArray_emptyString() {
        String input = "";
        byte[] expected = {};
        byte[] actual = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidLength() {
        String input = "YWJ";
        Base64.base64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidCharacter() {
        String input = "YWJ#";
        Base64.base64ToByteArray(input);
    }

    @Test
    public void testByteArrayToAltBase64_typicalCase() {
        byte[] input = { 97, 98, 99 };
        String expected = "YWJj";
        String actual = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToAltBase64_emptyArray() {
        byte[] input = {};
        String expected = "";
        String actual = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64_typicalCase() {
        byte[] input = { 97, 98, 99 };
        String expected = "YWJj";
        String actual = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToBase64_emptyArray() {
        byte[] input = {};
        String expected = "";
        String actual = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testMain_validInput() {
        String[] args = {};
        Base64.main(args);
    }

}