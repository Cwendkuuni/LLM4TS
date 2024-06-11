package org.apache.commons.codec.language;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void testEncode() {
        String encoded = soundex.encode("Apache");
        assertEquals("A121", encoded);
    }

    @Test
    public void testDifference() throws EncoderException {
        int difference = soundex.difference("Apache", "Epache");
        assertEquals(3, difference);
    }
    
    @Test(expected=EncoderException.class)
    public void testEncodeInvalidObject() throws EncoderException {
        soundex.encode(123);
    }
    
    @Test
    public void testSoundexNullInput() {
        assertEquals(null, soundex.soundex(null));
    }
    
    @Test
    public void testSoundexEmptyInput() {
        assertEquals("", soundex.soundex(""));
    }
    
    @Test
    public void testSoundexValidInput() {
        String encoded = soundex.soundex("Apache");
        assertEquals("A121", encoded);
    }
    
    @Test
    public void testSoundexUnmappedCharacter() {
        String encoded = soundex.soundex("A!");
        assertEquals("A000", encoded);
    }
    
    @Test
    public void testSoundexRepeatedCharacters() {
        String encoded = soundex.soundex("AA");
        assertEquals("A000", encoded);
    }
    
    @Test
    public void testSoundexLastCharacterMapped() {
        String encoded = soundex.soundex("ApacheS");
        assertEquals("A120", encoded);
    }
    
    @Test
    public void testGetMaxLength() {
        int maxLength = soundex.getMaxLength();
        assertEquals(4, maxLength);
    }
    
    @Test
    public void testSetMaxLength() {
        soundex.setMaxLength(5);
        int maxLength = soundex.getMaxLength();
        assertEquals(5, maxLength);
    }
    
    @Test
    public void testUS_ENGLISH_MAPPING_STRING() {
        String mappingString = Soundex.US_ENGLISH_MAPPING_STRING;
        assertEquals("01230120022455012623010202", mappingString);
    }
    
    @Test
    public void testUS_ENGLISH_MAPPING() {
        char[] mapping = Soundex.US_ENGLISH_MAPPING;
        assertEquals('0', mapping[0]);
        assertEquals('1', mapping[1]);
        assertEquals('2', mapping[2]);
    }
    
    @Test
    public void testUS_ENGLISH() {
        Soundex usEnglish = Soundex.US_ENGLISH;
        assertNotNull(usEnglish);
    }
    
    @Test
    public void testConstructorSoundexMapping() {
        char[] mapping = {'0', '1', '2'};
        Soundex soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }
    
    @Test
    public void testConstructorStringMapping() {
        String mapping = "012";
        Soundex soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }
    
    @Test
    public void testGetSoundexMapping() {
        char[] soundexMapping = soundex.getSoundexMapping();
        assertNotNull(soundexMapping);
    }
}