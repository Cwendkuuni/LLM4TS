import org.junit.Assert;
import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.altBase64ToByteArray(s);
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidLength() {
        String s = "SGVsbG8gd29ybGQ";
        Base64.altBase64ToByteArray(s);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.base64ToByteArray(s);
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidLength() {
        String s = "SGVsbG8gd29ybGQ";
        Base64.base64ToByteArray(s);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToAltBase64(bb);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] bb = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToBase64(bb);
        Assert.assertEquals(expected, actual);
    }

}