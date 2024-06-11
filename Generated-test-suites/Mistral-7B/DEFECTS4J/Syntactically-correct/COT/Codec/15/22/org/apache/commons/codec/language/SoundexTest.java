package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;
import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING;

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
        Assert.assertNotNull(soundex);
        Assert.assertArrayEquals(US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }

    /**
     * Test constructor Soundex(char[])
     */
    @Test
    public void testSoundexConstructorWithMapping() {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING);
        Assert.assertNotNull(soundex);
        Assert.assertArrayEquals(US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }

    /**
     * Test constructor Soundex(String)
     */
    @Test
    public void testSoundexConstructorWithMappingString() {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING_STRING);
        Assert.assertNotNull(soundex);
        Assert.assertArrayEquals(US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }

    /**
     * Test method encode
     */
    @Test
     public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        String input = "Apple";
        String expected = "K330";
        String output = soundex.encode(input);
        Assert.assertEquals(expected, output);
    }

    /**
     * Test method encode with null input
     */
    @Test (expected = EncoderException.class)
    public void testEncodeWithNullInput() {
        Soundex soundex = new Soundex();
        String input = null;
        soundex.encode(input);
    }

    /**
     * Test method encode with non-String input
     */
    @Test (expected = EncoderException.class)
    public void testEncodeWithNonStringInput() {
        Soundex soundex = new Soundex();
        Object input = 123;
        soundex.encode(input);
    }

    /**
     * Test method difference
     */
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int expected = 0;
        int output = soundex.difference(str1, str2);
        Assert.assertEquals(expected, output);
    }

    /**
     * Test method difference with equal strings
     */
    @Test
    public void testDifferenceWithEqualStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Apple";
        int expected = 4;
        int output = soundex.difference(str1, str2);
        Assert.assertEquals(expected, output);
    }

    /**
     * Test method difference with empty strings
     */
    @Test
    public void testDifferenceWithEmptyStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "";
        String str2 = "";
        int expected = 4;
        int output = soundex.difference(str1, str2);
        Assert.assertEquals(expected, output);
    }
}