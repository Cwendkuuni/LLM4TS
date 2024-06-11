package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        // Test null input
        assertNull(SoundexUtils.clean(null));
        
        // Test empty string input
        assertEquals("", SoundexUtils.clean(""));
        
        // Test no letters in input
        assertEquals("123", SoundexUtils.clean("123"));
        
        // Test lowercase letters in input
        assertEquals("ABC", SoundexUtils.clean("aBc"));
        
        // Test uppercase letters in input
        assertEquals("ABC", SoundexUtils.clean("ABC"));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        // Test null input
        assertEquals(0, SoundexUtils.difference(null, "SAME", "SAME"));
        
        // Test same encoded strings
        StringEncoder encoder = new Soundex();
        String s1 = encoder.encode("SAME");
        String s2 = encoder.encode("SAME");
        assertEquals(s1.length(), SoundexUtils.difference(encoder, "SAME", "SAME"));
        
        // Test different encoded strings with same length
        encoder = new RefinedSoundex();
        s1 = encoder.encode("DIFFERENT");
        s2 = encoder.encode("SIMILAR");
        assertEquals(0, SoundexUtils.difference(encoder, "DIFFERENT", "SIMILAR"));
        
        // Test different encoded strings with different lengths
        s1 = encoder.encode("DIFFER");
        s2 = encoder.encode("SIMILA");
        assertEquals(0, SoundexUtils.difference(encoder, "DIFFER", "SIMILA"));
    }
    
    @Test
    public void testDifferenceEncoded() {
        // Test null input
        assertEquals(0, SoundexUtils.differenceEncoded(null, "SAME"));
        
        // Test same encoded strings
        String s1 = "SAME";
        String s2 = "SAME";
        assertEquals(s1.length(), SoundexUtils.differenceEncoded(s1, s2));
        
        // Test different encoded strings with same length
        s1 = "DIFFERENT";
        s2 = "SIMILAR";
        assertEquals(0, SoundexUtils.differenceEncoded(s1, s2));
        
        // Test different encoded strings with different lengths
        s1 = "DIFFER";
        s2 = "SIMILA";
        assertEquals(0, SoundexUtils.differenceEncoded(s1, s2));
    }
}