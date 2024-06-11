package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;
import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test constructor Soundex().
     */
    @Test
    public void testSoundexConstructor() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    /**
     * Test constructor Soundex(char[]).
     */
    @Test
    public void testSoundexConstructorWithMapping() {
        char[] mapping = US_ENGLISH_MAPPING_STRING.toCharArray();
        Soundex soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }

    /**
     * Test constructor Soundex(String).
     */
    @Test
    public void testSoundexConstructorWithMappingString() {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING_STRING);
        assertNotNull(soundex);
    }

    /**
     * Test method difference.
     */
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int actual = soundex.difference(s1, s2);
        int expected = 0;
        assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apple";
        actual = soundex.difference(s1, s2);
        expected = 4;
        assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apricot";
        actual = soundex.difference(s1, s2);
        expected = 3;
        assertEquals(expected, actual);
    }

    /**
     * Test method encode.
     */
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String str = "Apple";
        String actual = soundex.encode(str);
        String expected = "A111";
        assertEquals(expected, actual);

        str = "Orange";
        actual = soundex.encode(str);
        expected = "O150";
        assertEquals(expected, actual);

        str = "Apricot";
        actual = soundex.encode(str);
        expected = "A160";
        assertEquals(expected, actual);

        str = "AppleSauce";
        actual = soundex.encode(str);
        expected = "A111A111";
        assertEquals(expected, actual);

        str = "";
        try {
            actual = soundex.encode(str);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    /**
     * Test method getMaxLength.
     */
    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int actual = soundex.getMaxLength();
        int expected = 4;
        assertEquals(expected, actual);

        soundex = new Soundex();
        try {
            soundex.setMaxLength(5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method getSoundexMapping.
     */
    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] actual = soundex.getSoundexMapping();
        char[] expected = US_ENGLISH_MAPPING;
        assertArrayEquals(expected, actual);
    }

    /**
     * Test method map.
     */
    @Test
    public void testMap() {
        Soundex soundex = new Soundex();
        char ch = 'A';
        char actual = soundex.map(ch);
        char expected = '1';
        assertEquals(expected, actual);

        ch = 'H';
        actual = soundex.map(ch);
        expected = '0';
        assertEquals(expected, actual);

        ch = 'W';
        actual = soundex.map(ch);
        expected = '0';
        assertEquals(expected, actual);

        ch = 'X';
        try {
            actual = soundex.map(ch);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method soundex.
     */
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String str = "Apple";
        String actual = soundex.soundex(str);
        String expected = "A111";
        assertEquals(expected, actual);

        str = "Orange";
        actual = soundex.soundex(str);
        expected = "O150";
        assertEquals(expected, actual);

        str = "Apricot";
        actual = soundex.soundex(str);
        expected = "A160";
        assertEquals(expected, actual);

        str = "AppleSauce";
        actual = soundex.soundex(str);
        expected = "A111A111";
        assertEquals(expected, actual);

        str = "";
        assertEquals("", soundex.soundex(str));

        str = "AppleApple";
        actual = soundex.soundex(str);
        expected = "A111A111";
        assertEquals(expected, actual);

        str = "AppleAppleApple";
        actual = soundex.soundex(str);
        expected = "A111A111A111";
        assertEquals(expected, actual);

        str = "AppleAppleAppleApple";
        actual = soundex.soundex(str);
        expected = "A111A111A111A111";
        assertEquals(expected, actual);

        str = "AppleAppleAppleAppleApple";
        actual = soundex.soundex(str);
        expected = "A111A111A111A111A111";
        assertEquals(expected, actual);

        str = "AppleAppleAppleAppleAppleApple";
        actual = soundex.soundex(str);
        expected = "A111A111A111A111A111A111";
        assertEquals(expected, actual);
    }
}