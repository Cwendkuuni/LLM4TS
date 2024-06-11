package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test method for {@link Soundex#Soundex()} constructors.
     */
    @Test
    public void testSoundexDefaultConstructor() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    /**
     * Test method for {@link Soundex#Soundex(char[])} constructors.
     */
    @Test
    public void testSoundexMappingConstructor() {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING);
        assertNotNull(soundex);
    }

    /**
     * Test method for {@link Soundex#Soundex(String)} constructors.
     */
    @Test
    public void testSoundexMappingStringConstructor() {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING_STRING);
        assertNotNull(soundex);
    }

    /**
     * Test method for {@link Soundex#difference(String, String)} method.
     */
    @Test
    public void testDifferenceMethod() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int result = soundex.difference(str1, str2);
        assertEquals(0, result);

        String str3 = "Apple";
        String str4 = "Apple";
        result = soundex.difference(str3, str4);
        assertEquals(4, result);
    }

    /**
     * Test method for {@link Soundex#encode(Object)} method.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeObjectThrowsException() {
        Soundex soundex = new Soundex();
        Object obj = new Object();
        soundex.encode(obj);
    }

    /**
     * Test method for {@link Soundex#encode(String)} method.
     */
    @Test
    public void testEncodeStringMethod() {
        Soundex soundex = new Soundex();
        String str = "Apple";
        String result = soundex.encode(str);
        assertEquals("A111", result);
    }

    /**
     * Test method for {@link Soundex#getMappingCode(String, int)} method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMappingCodeThrowsException() {
        Soundex soundex = new Soundex();
        String str = "Apple";
        soundex.getMappingCode(str, -1);
    }

    /**
     * Test method for {@link Soundex#map(char)} method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapThrowsException() {
        Soundex soundex = new Soundex();
        char ch = 'z';
        soundex.map(ch);
    }

    /**
     * Test method for {@link Soundex#soundex(String)} method.
     */
    @Test
    public void testSoundexMethod() {
        Soundex soundex = new Soundex();
        String str = "Apple";
        String result = soundex.soundex(str);
        assertEquals("A111", result);
    }
}