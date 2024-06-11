package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCharacter() {
        char c = '!';
        Base64._$23183(c, Base64._$23169);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringLengthNotMultipleOfFour() {
        String input = "SGVsb";
        Base64.altBase64ToByteArray(input);
    }

    @Test
    public void testExtraPaddingEquals() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }

    @Test
    public void testExtraPaddingDoubleEquals() {
        String input = "SGVsbG8==";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }

    @Test
    public void testMain() {
        // Not ideal to test main method directly, but for coverage purposes
        Base64.main(new String[]{"arg1"});
    }
}