package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    @Test
    public void testDefaultSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String originalString1 = "Apple";
        String originalString2 = "Application";
        String soundexCode1 = soundex.encode(originalString1);
        String soundexCode2 = soundex.encode(originalString2);

        // Test encoding the same string twice
        String soundexCode1Again = soundex.encode(originalString1);
        assertEquals(soundexCode1, soundexCode1Again);

        // Test encoding the same string with different Soundex instances
        Soundex soundexInstance = new Soundex(Soundex.US_ENGLISH_MAPPING);
        String soundexCode1FromNewInstance = soundexInstance.encode(originalString1);
        assertEquals(soundexCode1, soundexCode1FromNewInstance);

        // Test encoding the same string with different charsets
        String originalStringWithDiacritics = "Müller";
        String soundexCodeWithDiacritics = soundex.encode(originalStringWithDiacritics);
        Soundex soundexInstanceWithCustomMapping = new Soundex("12312001231200224550123123010202".toCharArray());
        String soundexCodeWithDiacriticsFromCustomMapping = soundexInstanceWithCustomMapping.encode(originalStringWithDiacritics);
        assertEquals(soundexCodeWithDiacritics, soundexCodeWithDiacriticsFromCustomMapping);

        // Test encoding different strings
        assertEquals(soundexCode1, soundex.encode(originalString2));

        // Test encoding empty string
        String emptyString = "";
        String soundexCodeEmpty = soundex.encode(emptyString);
        assertArrayEquals(new char[0], soundexCodeEmpty.toCharArray());

        // Test encoding null string
        String nullString = null;
        try {
            soundex.encode(nullString);
            fail("Expected EncoderException when encoding null string");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test(expected = EncoderException.class)
    public void testEncodingNullStringThrowsException() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        soundex.encode(null);
    }

    @Test
    public void testDifferenceMethod() {
        Soundex soundex = Soundex.US_ENGLISH;
        String string1 = "Application";
        String string2 = "Application Program Interface";
        int difference = soundex.difference(string1, string2);
        assertEquals(3, difference);

        String string3 = "Apple";
        String string4 = "Orange";
        difference = soundex.difference(string3, string4);
        assertEquals(0, difference);

        String string5 = "Apple";
        String string6 = "Apple";
        difference = soundex.difference(string5, string6);
        assertEquals(4, difference);

        String string7 = "";
        String string8 = "";
        difference = soundex.difference(string7, string8);
        assertEquals(0, difference);

        String string9 = null;
        String string10 = null;
        try {
            difference = soundex.difference(string9, string10);
            fail("Expected EncoderException when comparing null strings");
        } catch (EncoderException e) {
            // Expected
        }
    }
}