package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test lowercase letters
        assertEquals("1111111111", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));
        
        // Test uppercase letters
        assertEquals("1111111111", caverphone.caverphone("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        
        // Test letters with numbers
        assertEquals("1111111111", caverphone.caverphone("a1b2c3d4e5f6g7h8i9j0"));
        
        // Test letters with special characters
        assertEquals("1111111111", caverphone.caverphone("!@#$%^&*()"));
        
        // Test specific inputs
        assertEquals("1111111111", caverphone.caverphone("1234abcde"));
        assertEquals("4413411111", caverphone.caverphone("Caverphone"));
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", caverphone.encode(""));
        
        // Test lowercase letters
        assertEquals("1111111111", caverphone.encode("abcdefghijklmnopqrstuvwxyz"));
        
        // Test uppercase letters
        assertEquals("1111111111", caverphone.encode("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        
        // Test letters with numbers
        assertEquals("1111111111", caverphone.encode("a1b2c3d4e5f6g7h8i9j0"));
        
        // Test letters with special characters
        assertEquals("1111111111", caverphone.encode("!@#$%^&*()"));
        
        // Test specific inputs
        assertEquals("1111111111", caverphone.encode("1234abcde"));
        assertEquals("4413411111", caverphone.encode("Caverphone"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test equal caverphone codes
        assertTrue(caverphone.isCaverphoneEqual("Caverphone", "Caverphone"));
        
        // Test non-equal caverphone codes
        assertFalse(caverphone.isCaverphoneEqual("Caverphone", "Caverphone1"));
        assertFalse(caverphone.isCaverphoneEqual("Caverphone", ""));
    }
}