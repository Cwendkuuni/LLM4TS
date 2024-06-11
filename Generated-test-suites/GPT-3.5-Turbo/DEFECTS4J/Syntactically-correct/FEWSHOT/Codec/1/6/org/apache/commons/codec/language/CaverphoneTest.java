package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test lowercase input
        assertEquals("1111111111", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));
        
        // Test removing non-alphabetic characters
        assertEquals("1111111111", caverphone.caverphone("123!@#$%^&*()_+-="));
        
        // Test removing final "e"
        assertEquals("s123411111", caverphone.caverphone("se"));
        
        // Test replacements and handling start options
        assertEquals("krs3111111", caverphone.caverphone("coughroughtoughenoughtroughgnmb"));
        
        // Test specific replacements
        assertEquals("2qrs311111", caverphone.caverphone("cqci"));
        assertEquals("22rs311111", caverphone.caverphone("cecy"));
        assertEquals("3ch3111111", caverphone.caverphone("tchtch"));
        assertEquals("kg2fi31111", caverphone.caverphone("cqci"));
        assertEquals("st311111111", caverphone.caverphone("dph"));
        assertEquals("pp311111111", caverphone.caverphone("bb"));
        assertEquals("s2311111111", caverphone.caverphone("s"));
        
        // Test vowel replacements
        assertEquals("A3111111111", caverphone.caverphone("aeiou"));
        assertEquals("33gh3kh33kh3", caverphone.caverphone("3gh3gh3"));
        
        // Test removals
        assertEquals("31111111111", caverphone.caverphone("2222"));
        assertEquals("A1111111111", caverphone.caverphone("3$"));
        
        // Test truncation
        assertEquals("1111111111", caverphone.caverphone("1234567890"));
    }
    
    @Test(expected = EncoderException.class)
    public void testEncode_NotString() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }
    
    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", caverphone.encode(""));
        
        // Test input as string
        assertEquals("1111111111", caverphone.encode("abcdefghijklmnopqrstuvwxyz"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test equal caverphones
        assertTrue(caverphone.isCaverphoneEqual("foobar", "fobar"));
        
        // Test non-equal caverphones
        assertFalse(caverphone.isCaverphoneEqual("foobar", "foo"));
        
        // Test empty string
        assertTrue(caverphone.isCaverphoneEqual("", ""));
    }
}