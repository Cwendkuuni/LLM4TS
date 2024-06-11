package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        assertArrayEquals(Base64.altBase64ToByteArray("") , new byte[]{});
        assertArrayEquals(Base64.altBase64ToByteArray("AAB") , new byte[]{});
        assertArrayEquals(Base64.altBase64ToByteArray("A==") , new byte[]{0});
        assertArrayEquals(Base64.altBase64ToByteArray("AA==") , new byte[]{0});
        assertArrayEquals(Base64.altBase64ToByteArray("AAA=") , new byte[]{0});
        assertArrayEquals(Base64.altBase64ToByteArray("AQ==") , new byte[]{1});        
    }

    @Test
    public void testBase64ToByteArray() {
        assertArrayEquals(Base64.base64ToByteArray("") , new byte[]{});
        assertArrayEquals(Base64.base64ToByteArray("AA==") , new byte[]{0});
        assertArrayEquals(Base64.base64ToByteArray("GihHZGpsaXN0") , new byte[]{-16, -127, -128, -127, -128, -127, -128});
        assertArrayEquals(Base64.base64ToByteArray("MTIzNDU2Nzg=") , new byte[]{49, 50, 51, 52, 53, 54, 55, 56});
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        assertEquals(Base64.byteArrayToAltBase64(new byte[]{}) , "");
        assertEquals(Base64.byteArrayToAltBase64(new byte[]{0}) , "A==");
        assertEquals(Base64.byteArrayToAltBase64(new byte[]{-1, -128, 0}) , "/gA=");
        assertEquals(Base64.byteArrayToAltBase64(new byte[]{49, 50, 51, 52, 53, 54, 55, 56}) , "MTIzNDU2Nzg=");
    }

    @Test
    public void testByteArrayToBase64() {
        assertEquals(Base64.byteArrayToBase64(new byte[]{}) , "");
        assertEquals(Base64.byteArrayToBase64(new byte[]{0}) , "AA==");
        assertEquals(Base64.byteArrayToBase64(new byte[]{-16, -127, -128, -127, -128, -127, -128}) , "GihHZGpsaXN0");
        assertEquals(Base64.byteArrayToBase64(new byte[]{49, 50, 51, 52, 53, 54, 55, 56}) , "MTIzNDU2Nzg=");
    }
}