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
        
        // Test lowercase conversion
        assertEquals("ka1v1f1n111111", caverphone.caverphone("KaVUfEn"));
        
        // Test removal of non-alphabetic characters
        assertEquals("lkw", caverphone.caverphone("1*\\/kW"));
        
        // Test removal of final 'e'
        assertEquals("m1111111111", caverphone.caverphone("me"));
        
        // Test replacements
        assertEquals("kAnAnes2", caverphone.caverphone("kanace"));
        assertEquals("kAnAkEs2", caverphone.caverphone("kanacek"));
        
        // Test removals
        assertEquals("hA1nA", caverphone.caverphone("hanh"));
        assertEquals("2i22Ap1", caverphone.caverphone("hihi"));
        
        // Test code generation
        assertEquals("kA1dRf1555", caverphone.caverphone("coderfun"));
        
        // Test string comparison
        assertTrue(caverphone.isCaverphoneEqual("ka1v1f1n", "caverfun"));
        assertFalse(caverphone.isCaverphoneEqual("ko1d2f02", "coderfun"));
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringParameter() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(1234);
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test encoding
        assertEquals("ko3d3ff", caverphone.encode("coderfun"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test string comparison
        assertTrue(caverphone.isCaverphoneEqual("ka1v1f1n", "caverfun"));
        assertFalse(caverphone.isCaverphoneEqual("ko1d2f02", "coderfun"));
    }
}