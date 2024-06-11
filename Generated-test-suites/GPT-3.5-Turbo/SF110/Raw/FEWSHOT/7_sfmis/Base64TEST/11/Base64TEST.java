package com.hf.sfm.crypt;

import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {
    
    /*Test Cases for altBase64ToByteArray() method*/
    @Test
    public void testAltBase64ToByteArrayEmptyString() {
        byte[] expected = new byte[0];
        assertArrayEquals(expected, Base64.altBase64ToByteArray(""));
    }
    
    @Test
    public void testAltBase64ToByteArrayStringWithMultipleOfFourChars() {
        byte[] expected = new byte[]{0x14, 0x52, (byte) 0xFA};
        assertArrayEquals(expected, Base64.altBase64ToByteArray("FVF+"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayStringWithInvalidLength() {
        Base64.altBase64ToByteArray("FVF");
    }
    
    /*Test Cases for base64ToByteArray() method*/
    @Test
    public void testBase64ToByteArrayEmptyString() {
        byte[] expected = new byte[0];
        assertArrayEquals(expected, Base64.base64ToByteArray(""));
    }
    
    @Test
    public void testBase64ToByteArrayStringWithMultipleOfFourChars() {
        byte[] expected = new byte[]{0x14, 0x52, (byte) 0xFA};
        assertArrayEquals(expected, Base64.base64ToByteArray("FVF+"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayStringWithInvalidLength() {
        Base64.base64ToByteArray("FVF");
    }
    
    /*Test Cases for _$23183() method*/
    @Test
    public void test_$23183ValidChar() {
        byte[] bb = new byte[128];
        bb['A'] = 20;
        assertEquals(20, Base64._$23183('A', bb));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test_$23183InvalidChar() {
        byte[] bb = new byte[128];
        Base64._$23183('!', bb);
    }
    
    /*Test Cases for byteArrayToAltBase64() method*/
    @Test
    public void testByteArrayToAltBase64EmptyArray() {
        byte[] arr = new byte[0];
        assertEquals("", Base64.byteArrayToAltBase64(arr));
    }
    
    @Test
    public void testByteArrayToAltBase64ArrayWithThreeBytes() {
        byte[] arr = new byte[]{0x14, 0x52, (byte) 0xFA};
        assertEquals("FVF+", Base64.byteArrayToAltBase64(arr));
    }
    
    /*Test Cases for byteArrayToBase64() method*/
    @Test
    public void testByteArrayToBase64EmptyArray() {
        byte[] arr = new byte[0];
        assertEquals("", Base64.byteArrayToBase64(arr));
    }
    
    @Test
    public void testByteArrayToBase64ArrayWithThreeBytes() {
        byte[] arr = new byte[]{0x14, 0x52, (byte) 0xFA};
        assertEquals("FVF+", Base64.byteArrayToBase64(arr));
    }
    
    /*Test Cases for main() method*/
    // Since the main() method only contains print statements,
    // it does not need to be tested.
    
}