package com.hf.sfm.crypt;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_basic() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray_basic() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64_basic() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64_basic() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }

    // Add more test cases for edge cases and error scenarios

}