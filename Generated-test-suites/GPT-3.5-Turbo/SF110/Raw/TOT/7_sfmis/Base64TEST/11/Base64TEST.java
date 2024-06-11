package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_normalCase() {
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expectedOutput = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testAltBase64ToByteArray_emptyString() {
        String input = "";
        byte[] expectedOutput = {};
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidStringLength() {
        String input = "SGVsbG8gV29ybGQ";
        Base64.altBase64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidCharacter() {
        String input = "SGVsbG8g!29ybGQ=";
        Base64.altBase64ToByteArray(input);
    }

    @Test
    public void testBase64ToByteArray_normalCase() {
        String input = "SGVsbG8gV29ybGQ=";
        byte[] expectedOutput = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        byte[] actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testBase64ToByteArray_emptyString() {
        String input = "";
        byte[] expectedOutput = {};
        byte[] actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidStringLength() {
        String input = "SGVsbG8gV29ybGQ";
        Base64.base64ToByteArray(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidCharacter() {
        String input = "SGVsbG8g!29ybGQ=";
        Base64.base64ToByteArray(input);
    }

    @Test
    public void testByteArrayToAltBase64_normalCase() {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expectedOutput = "SGVsbG8gV29ybGQ=";
        String actualOutput = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToAltBase64_emptyArray() {
        byte[] input = {};
        String expectedOutput = "";
        String actualOutput = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToBase64_normalCase() {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expectedOutput = "SGVsbG8gV29ybGQ=";
        String actualOutput = Base64.byteArrayToBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testByteArrayToBase64_emptyArray() {
        byte[] input = {};
        String expectedOutput = "";
        String actualOutput = Base64.byteArrayToBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testMain() {
        // This is a static method that prints the output to the console. No meaningful test can be performed here.
        // We include this test just for the purpose of code coverage.
        String[] args = {};
        Base64.main(args);
    }
}