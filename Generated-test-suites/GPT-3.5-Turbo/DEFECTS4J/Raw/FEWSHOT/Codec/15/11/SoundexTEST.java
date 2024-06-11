package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SoundexTest {
    
    private Soundex soundex;
    
    @Before
    public void setUp() {
        soundex = new Soundex();
    }
    
    @Test
    public void testDifference() throws Exception {
        assertEquals(0, soundex.difference(null, null));
        assertEquals(0, soundex.difference("", ""));
        assertEquals(0, soundex.difference("abc", "abc"));
        assertEquals(4, soundex.difference("abc", "abcde"));
        assertEquals(2, soundex.difference("abc", "def"));
    }
    
    @Test(expected = EncoderException.class)
    public void testEncode_NotString() throws Exception {
        soundex.encode(123);
    }
    
    @Test
    public void testEncode_Null() throws Exception {
        assertNull(soundex.encode(null));
    }
    
    @Test
    public void testEncode_EmptyString() throws Exception {
        assertEquals("0000", soundex.encode(""));
    }
    
    @Test
    public void testEncode_LowerCase() throws Exception {
        assertEquals("A123", soundex.encode("abc"));
    }
    
    @Test
    public void testEncode_UpperCase() throws Exception {
        assertEquals("A123", soundex.encode("ABC"));
    }
    
    @Test
    public void testEncode_DoesNotEncode() throws Exception {
        assertEquals("A123", soundex.encode("AEIOU"));
        assertEquals("B123", soundex.encode("BCDFG"));
        assertEquals("C123", soundex.encode("CEHKMNPRS"));
        assertEquals("D123", soundex.encode("DT"));
        assertEquals("E123", soundex.encode("EY"));
        assertEquals("F123", soundex.encode("FV"));
        assertEquals("G123", soundex.encode("GJ"));
        assertEquals("H123", soundex.encode("H"));
        assertEquals("J123", soundex.encode("JZ"));
        assertEquals("K123", soundex.encode("KL"));
        assertEquals("L123", soundex.encode("L"));
        assertEquals("M123", soundex.encode("MN"));
        assertEquals("N123", soundex.encode("N"));
        assertEquals("P123", soundex.encode("PW"));
        assertEquals("Q123", soundex.encode("QUX"));
        assertEquals("R123", soundex.encode("R"));
        assertEquals("S123", soundex.encode("S"));
        assertEquals("T123", soundex.encode("T"));
        assertEquals("U123", soundex.encode("UIOY"));
        assertEquals("V123", soundex.encode("VB"));
        assertEquals("W123", soundex.encode("W"));
        assertEquals("X123", soundex.encode("XC"));
        assertEquals("Y123", soundex.encode("Y"));
        assertEquals("Z123", soundex.encode("Z"));
    }
    
    @Test
    public void testSetMaxLength() {
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }
    
    @Test
    public void testGetSoundexMapping() {
        char[] mapping = soundex.getSoundexMapping();
        assertEquals("01230120022455012623010202", new String(mapping));
    }
    
    @Test
    public void testSoundex_Null() throws Exception {
        assertNull(soundex.soundex(null));
    }
    
    @Test
    public void testSoundex_EmptyString() throws Exception {
        assertEquals("0000", soundex.soundex(""));
    }
    
    @Test
    public void testSoundex_LowerCase() throws Exception {
        assertEquals("A123", soundex.soundex("abc"));
    }
    
    @Test
    public void testSoundex_UpperCase() throws Exception {
        assertEquals("A123", soundex.soundex("ABC"));
    }
    
    @Test
    public void testSoundex_DoesNotEncode() throws Exception {
        assertEquals("A123", soundex.soundex("AEIOU"));
        assertEquals("B123", soundex.soundex("BCDFG"));
        assertEquals("C123", soundex.soundex("CEHKMNPRS"));
        assertEquals("D123", soundex.soundex("DT"));
        assertEquals("E123", soundex.soundex("EY"));
        assertEquals("F123", soundex.soundex("FV"));
        assertEquals("G123", soundex.soundex("GJ"));
        assertEquals("H123", soundex.soundex("H"));
        assertEquals("J123", soundex.soundex("JZ"));
        assertEquals("K123", soundex.soundex("KL"));
        assertEquals("L123", soundex.soundex("L"));
        assertEquals("M123", soundex.soundex("MN"));
        assertEquals("N123", soundex.soundex("N"));
        assertEquals("P123", soundex.soundex("PW"));
        assertEquals("Q123", soundex.soundex("QUX"));
        assertEquals("R123", soundex.soundex("R"));
        assertEquals("S123", soundex.soundex("S"));
        assertEquals("T123", soundex.soundex("T"));
        assertEquals("U123", soundex.soundex("UIOY"));
        assertEquals("V123", soundex.soundex("VB"));
        assertEquals("W123", soundex.soundex("W"));
        assertEquals("X123", soundex.soundex("XC"));
        assertEquals("Y123", soundex.soundex("Y"));
        assertEquals("Z123", soundex.soundex("Z"));
    }

}