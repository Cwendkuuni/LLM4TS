package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String s = "ABCDEFG";
        byte[] expected = {-65, -66, -67, -68, -69, -70, -71};
        byte[] result = Base64.altBase64ToByteArray(s);
        Assert.assertArrayEquals(expected, result);

        s = "";
        expected = new byte[0];
        result = Base64.altBase64ToByteArray(s);
        Assert.assertArrayEquals(expected, result);

        // Add more test cases...
    }

    @Test
    public void testBase64ToByteArray() {
        String s = "ABCDEFG";
        byte[] expected = {-65, -66, -67, -68, -69, -70, -71};
        byte[] result = Base64.base64ToByteArray(s);
        Assert.assertArrayEquals(expected, result);

        s = "";
        expected = new byte[0];
        result = Base64.base64ToByteArray(s);
        Assert.assertArrayEquals(expected, result);

        // Add more test cases...
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = {-65, -66, -67, -68, -69, -70, -71};
        String expected = "ABCDEFG";
        String result = Base64.byteArrayToAltBase64(bb);
        Assert.assertEquals(expected, result);

        bb = new byte[0];
        expected = "";
        result = Base64.byteArrayToAltBase64(bb);
        Assert.assertEquals(expected, result);

        // Add more test cases...
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] bb = {-65, -66, -67, -68, -69, -70, -71};
        String expected = "QUJDREVGRw==";
        String result = Base64.byteArrayToBase64(bb);
        Assert.assertEquals(expected, result);

        bb = new byte[0];
        expected = "";
        result = Base64.byteArrayToBase64(bb);
        Assert.assertEquals(expected, result);

        // Add more test cases...
    }
}