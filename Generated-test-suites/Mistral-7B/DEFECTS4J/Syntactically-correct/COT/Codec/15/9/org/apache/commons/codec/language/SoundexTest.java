package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;

/**
 * Test class for Soundex class
 */
public class SoundexTest {

    /**
     * Test constructor Soundex()
     */
    @Test
    public void testSoundexConstructor() {
        Soundex soundex = new Soundex();
        assertEquals(Soundex.US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }

    /**
     * Test constructor Soundex(char[])
     */
    @Test
    public void testSoundexConstructorWithMapping() {
        char[] customMapping = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        Soundex soundex = new Soundex(customMapping);
        assertNotEquals(Soundex.US_ENGLISH_MAPPING, soundex.getSoundexMapping());
        assertEquals(customMapping, soundex.getSoundexMapping());
    }

    /**
     * Test constructor Soundex(String)
     */
    @Test
    public void testSoundexConstructorWithMappingString() {
        Soundex soundex = new Soundex("1230120022455012623010202");
        assertEquals(US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }

    /**
     * Test method difference
     */
    @Test
    public void testDifference() throws EncoderException {
        String s1 = "Apple";
        String s2 = "Orange";
        Soundex soundex = new Soundex();
        int difference = soundex.difference(s1, s2);
        assertEquals(0, difference);

        String s3 = "Apple";
        String s4 = "Apple";
        difference = soundex.difference(s3, s4);
        assertEquals(4, difference);
    }

    /**
     * Test method encode
     */
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String str = "Apple";
        String encoded = soundex.encode(str);
        assertEquals("A231", encoded);

        String str2 = "abCdEf";
        assertThrows(EncoderException.class, () -> soundex.encode(str2));
    }

    /**
     * Test method soundex
     */
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String input = "Apple";
        String expectedOutput = "A231";
        String output = soundex.soundex(input);
        assertEquals(expectedOutput, output);
    }
}