import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray1() {
        String s = "YWJjZA==";
        byte[] expected = {97, 98, 99, 100};
        byte[] actual = Base64.altBase64ToByteArray(s);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testAltBase64ToByteArray2() {
        String s = "ZG9uZQo=";
        byte[] expected = {100, 111, 110, 101, 10};
        byte[] actual = Base64.altBase64ToByteArray(s);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testAltBase64ToByteArray3() {
        String s = "cGFzc3dvcmQ=";
        byte[] expected = {112, 97, 115, 115, 119, 111, 114, 100};
        byte[] actual = Base64.altBase64ToByteArray(s);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray1() {
        String s = "YWJjZA==";
        byte[] expected = {97, 98, 99, 100};
        byte[] actual = Base64.base64ToByteArray(s);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray2() {
        String s = "ZG9uZQo=";
        byte[] expected = {100, 111, 110, 101, 10};
        byte[] actual = Base64.base64ToByteArray(s);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray3() {
        String s = "cGFzc3dvcmQ=";
        byte[] expected = {112, 97, 115, 115, 119, 111, 114, 100};
        byte[] actual = Base64.base64ToByteArray(s);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testByteArrayToAltBase641() {
        byte[] bb = {97, 98, 99, 100};
        String expected = "YWJjZA==";
        String actual = Base64.byteArrayToAltBase64(bb);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToAltBase642() {
        byte[] bb = {100, 111, 110, 101, 10};
        String expected = "ZG9uZQo=";
        String actual = Base64.byteArrayToAltBase64(bb);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToAltBase643() {
        byte[] bb = {112, 97, 115, 115, 119, 111, 114, 100};
        String expected = "cGFzc3dvcmQ=";
        String actual = Base64.byteArrayToAltBase64(bb);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase641() {
        byte[] bb = {97, 98, 99, 100};
        String expected = "YWJjZA==";
        String actual = Base64.byteArrayToBase64(bb);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase642() {
        byte[] bb = {100, 111, 110, 101, 10};
        String expected = "ZG9uZQo=";
        String actual = Base64.byteArrayToBase64(bb);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase643() {
        byte[] bb = {112, 97, 115, 115, 119, 111, 114, 100};
        String expected = "cGFzc3dvcmQ=";
        String actual = Base64.byteArrayToBase64(bb);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void test_$23183() {
        char c = 'a';
        byte[] bb = {};
        int expected = 97;
        int actual = Base64._$23183(c, bb);
        Assert.assertEquals(expected, actual);
    }

}