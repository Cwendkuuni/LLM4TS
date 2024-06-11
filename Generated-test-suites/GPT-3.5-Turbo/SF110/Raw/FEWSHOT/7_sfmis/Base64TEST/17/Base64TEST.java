
package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        assertArrayEquals(new byte[] { 109, 97, 110 }, Base64.altBase64ToByteArray("bWFu"));
        assertArrayEquals(new byte[] { 102, 111, 111 }, Base64.altBase64ToByteArray("Zm9v"));
        assertArrayEquals(new byte[] { 98, 97, 114 }, Base64.altBase64ToByteArray("YmFy"));
        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111 }, Base64.altBase64ToByteArray("aGVsbG8="));
        assertArrayEquals(new byte[] { 1, 2, 3 }, Base64.altBase64ToByteArray("AQID"));
    }

    @Test
    public void testBase64ToByteArray() {
        assertArrayEquals(new byte[] { 49, 50, 51 }, Base64.base64ToByteArray("MTIz"));
        assertArrayEquals(new byte[] { 97, 98, 99 }, Base64.base64ToByteArray("YWJj"));
        assertArrayEquals(new byte[] { 65, 66, 67 }, Base64.base64ToByteArray("QUJD"));
        assertArrayEquals(new byte[] { 72, 101, 108, 108, 111 }, Base64.base64ToByteArray("SGVsbG8="));
        assertArrayEquals(new byte[] { -51, -17, -68 }, Base64.base64ToByteArray("-POvgA=="));
    }

    @Test
    public void testByteArrayToAltBase64() {
        assertEquals("bWFu", Base64.byteArrayToAltBase64(new byte[] { 109, 97, 110 }));
        assertEquals("Zm9v", Base64.byteArrayToAltBase64(new byte[] { 102, 111, 111 }));
        assertEquals("YmFy", Base64.byteArrayToAltBase64(new byte[] { 98, 97, 114 }));
        assertEquals("aGVsbG8=", Base64.byteArrayToAltBase64(new byte[] { 104, 101, 108, 108, 111 }));
        assertEquals("AQID", Base64.byteArrayToAltBase64(new byte[] { 1, 2, 3 }));
    }

    @Test
    public void testByteArrayToBase64() {
        assertEquals("MTIz", Base64.byteArrayToBase64(new byte[] { 49, 50, 51 }));
        assertEquals("YWJj", Base64.byteArrayToBase64(new byte[] { 97, 98, 99 }));
        assertEquals("QUJD", Base64.byteArrayToBase64(new byte[] { 65, 66, 67 }));
        assertEquals("SGVsbG8=", Base64.byteArrayToBase64(new byte[] { 72, 101, 108, 108, 111 }));
        assertEquals("-POvgA==", Base64.byteArrayToBase64(new byte[] { -51, -17, -68 }));
    }

}
