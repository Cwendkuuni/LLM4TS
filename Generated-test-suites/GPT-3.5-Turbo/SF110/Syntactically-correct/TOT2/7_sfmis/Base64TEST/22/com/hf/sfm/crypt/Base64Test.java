package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String input1 = "Hello";
        byte[] expectedOutput1 = {72, 101, 108, 108, 111};
        assertArrayEquals(expectedOutput1, Base64.altBase64ToByteArray(input1));

        // Edge case
        String input2 = "World";
        byte[] expectedOutput2 = {87, 111, 114, 108, 100};
        assertArrayEquals(expectedOutput2, Base64.altBase64ToByteArray(input2));

        // Error scenario
        String input3 = "Invalid";
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.altBase64ToByteArray(input3);
        });
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        String input1 = "SGVsbG8=";
        byte[] expectedOutput1 = {72, 101, 108, 108, 111};
        assertArrayEquals(expectedOutput1, Base64.base64ToByteArray(input1));

        // Edge case
        String input2 = "V29ybGQ=";
        byte[] expectedOutput2 = {87, 111, 114, 108, 100};
        assertArrayEquals(expectedOutput2, Base64.base64ToByteArray(input2));

        // Error scenario
        String input3 = "Invalid";
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.base64ToByteArray(input3);
        });
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input1 = {72, 101, 108, 108, 111};
        String expectedOutput1 = "SGVsbG8=";
        assertEquals(expectedOutput1, Base64.byteArrayToAltBase64(input1));

        // Edge case
        byte[] input2 = {87, 111, 114, 108, 100};
        String expectedOutput2 = "V29ybGQ=";
        assertEquals(expectedOutput2, Base64.byteArrayToAltBase64(input2));
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input1 = {72, 101, 108, 108, 111};
        String expectedOutput1 = "SGVsbG8=";
        assertEquals(expectedOutput1, Base64.byteArrayToBase64(input1));

        // Edge case
        byte[] input2 = {87, 111, 114, 108, 100};
        String expectedOutput2 = "V29ybGQ=";
        assertEquals(expectedOutput2, Base64.byteArrayToBase64(input2));
    }

    // Additional test cases for other methods in the Base64 class can be added here

}