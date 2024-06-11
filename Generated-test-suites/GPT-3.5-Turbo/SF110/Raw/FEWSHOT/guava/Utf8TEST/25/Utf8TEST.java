package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        assertEquals(0, Utf8.encodedLength(""));
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(2, Utf8.encodedLength("€"));
        assertEquals(3, Utf8.encodedLength("𐐷"));
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(0, Utf8.encodedLengthGeneral("", 0));
        assertEquals(1, Utf8.encodedLengthGeneral("a", 0));
        assertEquals(2, Utf8.encodedLengthGeneral("€", 0));
        assertEquals(3, Utf8.encodedLengthGeneral("𐐷", 0));
    }
    
    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[] {}));
        assertTrue(Utf8.isWellFormed(new byte[] { 97 }));
        assertTrue(Utf8.isWellFormed(new byte[] { (byte) 0xe2, (byte) 0x82, (byte) 0xac }));
        assertTrue(Utf8.isWellFormed(new byte[] { (byte) 0xf0, (byte) 0x90, (byte) 0x90, (byte) 0xb7 }));
        assertFalse(Utf8.isWellFormed(new byte[] { -1 }));
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 0xc2, -1 }));
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 0xe2, (byte) 0x28, -1 }));
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 0xf0, (byte) 0x28, (byte) 0x8c, (byte) 0x3a }));
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[] {};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
        
        bytes = new byte[] { 97 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
        
        bytes = new byte[] { (byte) 0xe2, (byte) 0x82, (byte) 0xac };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
        
        bytes = new byte[] { (byte) 0xf0, (byte) 0x90, (byte) 0x90, (byte) 0xb7 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
        
        bytes = new byte[] { -1 };
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
        
        bytes = new byte[] { (byte) 0xc2, -1 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
        
        bytes = new byte[] { (byte) 0xe2, (byte) 0x28, -1 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[] { (byte) 0xf0, (byte) 0x28, (byte) 0x8c, (byte) 0x3a };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }
}