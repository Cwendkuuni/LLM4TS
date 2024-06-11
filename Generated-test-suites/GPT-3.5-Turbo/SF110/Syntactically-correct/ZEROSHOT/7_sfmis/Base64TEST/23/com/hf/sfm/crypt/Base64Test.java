package com.hf.sfm.crypt;

import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actual = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] actual = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] b = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToAltBase64(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] b = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToBase64(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        String s = "0123456789";
        byte[] b = s.getBytes();
        String expected = "MDEyMzQ1Njc4OQ==";
        Base64.main(null);
        byte[] actual = Base64.base64ToByteArray(expected);
        assertArrayEquals(b, actual);
    }
}