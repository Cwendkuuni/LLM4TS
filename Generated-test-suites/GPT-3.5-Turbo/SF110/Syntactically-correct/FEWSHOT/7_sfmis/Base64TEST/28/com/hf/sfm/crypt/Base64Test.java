package com.hf.sfm.crypt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        assertEquals(new byte[]{1, 2, 3}, Base64.altBase64ToByteArray("AQID"));
        assertEquals(new byte[]{65, 66, 67, 68}, Base64.altBase64ToByteArray("QUJDRA=="));
        assertEquals(new byte[]{49, 50, 51, 52, 53, 54, 55, 56, 57}, Base64.altBase64ToByteArray("MTIzNDU2Nzg5"));
        assertNull(Base64.altBase64ToByteArray(null));
    }

    @Test
    public void testBase64ToByteArray() {
        assertEquals(new byte[]{1, 2, 3}, Base64.base64ToByteArray("AQID"));
        assertEquals(new byte[]{65, 66, 67, 68}, Base64.base64ToByteArray("QUJDRA=="));
        assertEquals(new byte[]{49, 50, 51, 52, 53, 54, 55, 56, 57}, Base64.base64ToByteArray("MTIzNDU2Nzg5"));
        assertNull(Base64.base64ToByteArray(null));
    }

    @Test
    public void testByteArrayToAltBase64() {
        assertEquals("AQID", Base64.byteArrayToAltBase64(new byte[]{1, 2, 3}));
        assertEquals("QUJDRA==", Base64.byteArrayToAltBase64(new byte[]{65, 66, 67, 68}));
        assertEquals("MTIzNDU2Nzg5", Base64.byteArrayToAltBase64(new byte[]{49, 50, 51, 52, 53, 54, 55, 56, 57}));
        assertEquals("", Base64.byteArrayToAltBase64(new byte[]{}));
    }

    @Test
    public void testByteArrayToBase64() {
        assertEquals("AQID", Base64.byteArrayToBase64(new byte[]{1, 2, 3}));
        assertEquals("QUJDRA==", Base64.byteArrayToBase64(new byte[]{65, 66, 67, 68}));
        assertEquals("MTIzNDU2Nzg5", Base64.byteArrayToBase64(new byte[]{49, 50, 51, 52, 53, 54, 55, 56, 57}));
        assertEquals("", Base64.byteArrayToBase64(new byte[]{}));
    }
}