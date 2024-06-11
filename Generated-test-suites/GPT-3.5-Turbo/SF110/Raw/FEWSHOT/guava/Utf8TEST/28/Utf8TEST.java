import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Utf8Test {
 
    @Test
    public void testEncodedLength() {
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(6, Utf8.encodedLength("日本語"));
        assertEquals(4, Utf8.encodedLength("€€€€"));
        assertEquals(4, Utf8.encodedLength("$#@!"));
        assertEquals(1, Utf8.encodedLength(""));
    }
    
    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = {(byte)0x61, (byte)0x62, (byte)0x63};
        assertEquals(true, Utf8.isWellFormed(bytes1));
        
        byte[] bytes2 = {(byte)0xE6, (byte)0x97, (byte)0xA5, (byte)0xE6, (byte)0x9C, (byte)0xAC, (byte)0xE8, (byte)0xAA, (byte)0x9E};
        assertEquals(true, Utf8.isWellFormed(bytes2));
        
        byte[] bytes3 = {(byte)0xE2, (byte)0x82, (byte)0xAC, (byte)0xE2, (byte)0x82, (byte)0xAC, (byte)0xE2, (byte)0x82, (byte)0xAC, (byte)0xE2, (byte)0x82, (byte)0xAC};
        assertEquals(true, Utf8.isWellFormed(bytes3));
        
        byte[] bytes4 = {(byte)0x24, (byte)0x23, (byte)0x40};
        assertEquals(true, Utf8.isWellFormed(bytes4));
        
        byte[] bytes5 = {};
        assertEquals(true, Utf8.isWellFormed(bytes5));
    }
    
    @Test
    public void testIsWellFormed_Invalid() {
        byte[] bytes1 = {(byte)0xE6, (byte)0x97, (byte)0xA5, (byte)0xE6, (byte)0x9C, (byte)0xAC, (byte)0xE8, (byte)0xAA};
        assertEquals(false, Utf8.isWellFormed(bytes1));
        
        byte[] bytes2 = {(byte)0xE2, (byte)0x82, (byte)0xAC, (byte)0xE2, (byte)0x82, (byte)0xAC, (byte)0xE2, (byte)0x82, (byte)0xAC, (byte)0xE2, (byte)0x82, (byte)0xFE};
        assertEquals(false, Utf8.isWellFormed(bytes2));
        
        byte[] bytes3 = {(byte)0xE2, (byte)0x82};
        assertEquals(false, Utf8.isWellFormed(bytes3));
        
        byte[] bytes4 = {(byte)0xE6, (byte)0x97, (byte)0xA5, (byte)0xE6, (byte)0x9C};
        assertEquals(false, Utf8.isWellFormed(bytes4));
        
        byte[] bytes5 = {(byte)0xE6, (byte)0x97, (byte)0xA5};
        assertEquals(false, Utf8.isWellFormed(bytes5));
    }
    
}