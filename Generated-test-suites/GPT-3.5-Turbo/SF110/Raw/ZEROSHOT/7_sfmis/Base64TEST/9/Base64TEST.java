
package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void altBase64ToByteArray_ValidInput_ReturnsByteArray() {
        String input = "SGVsbG8gd29ybGQh";
        byte[] expected = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        byte[] actual = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void altBase64ToByteArray_InvalidLength_ThrowsIllegalArgumentException() {
        String input = "SGVsb";
        Base64.altBase64ToByteArray(input);
    }
    
    @Test
    public void base64ToByteArray_ValidInput_ReturnsByteArray() {
        String input = "SGVsbG8gd29ybGQh";
        byte[] expected = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        byte[] actual = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void base64ToByteArray_InvalidLength_ThrowsIllegalArgumentException() {
        String input = "SGVsb";
        Base64.base64ToByteArray(input);
    }
    
    @Test
    public void byteArrayToAltBase64_ValidInput_ReturnsBase64() {
        byte[] input = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        String expected = "SGVsbG8gd29ybGQh";
        String actual = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void byteArrayToBase64_ValidInput_ReturnsBase64() {
        byte[] input = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
        String expected = "SGVsbG8gd29ybGQh";
        String actual = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void _$23183_ValidInput_ReturnsByte() {
        char c = 'A';
        byte[] bb = Base64._$23169;
        byte expected = -1;
        byte actual = Base64._$23183(c, bb);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void _$23183_InvalidInput_ThrowsIllegalArgumentException() {
        char c = '#';
        byte[] bb = Base64._$23169;
        Base64._$23183(c, bb);
    }
}
