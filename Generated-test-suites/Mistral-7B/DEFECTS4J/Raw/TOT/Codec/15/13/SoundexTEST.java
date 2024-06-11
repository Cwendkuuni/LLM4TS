package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

/**
 * Tests for Soundex class.
 */
public class SoundexTest {

    /**
     * Test encodes and decodes the same string.
     */
    @Test
    public void testSameString() {
        Soundex soundex = new Soundex();
        String str = "test";
        String encoded = soundex.encode(str);
        String decoded = soundex.decode(encoded);
        assertEquals(str, decoded);
    }

    /**
     * Test encodes and decodes different strings.
     */
    @Test
    public void testDifferentStrings() {
        Soundex soundex = new Soundex();
        String str1 = "test";
        String str2 = "example";
        String encoded1 = soundex.encode(str1);
        String encoded2 = soundex.encode(str2);
        String decoded1 = soundex.decode(encoded1);
        String decoded2 = soundenc.decode(encoded2);
        assertEquals(str1, decoded1);
        assertEquals(str2, decoded2);
        int difference = soundex.difference(str1, str2);
        assertEquals(2, difference);
    }

    /**
     * Test encoding null string.
     */
    @Test(expected = EncoderException.class)
    public void testNullString() {
        Soundex soundex = new Soundex();
        String str = null;
        String encoded = soundex.encode(str);
    }

    /**
     * Test encoding empty string.
     */
    @Test
    public void testEmptyString() {
        Soundex soundex = new Soundex();
        String str = "";
        String encoded = soundex.encode(str);
        assertEquals("", encoded);
    }

    /**
     * Test encoding string with special characters.
     */
    @Test
    public void testSpecialCharacters() {
        Soundex soundex = new Soundex();
        String str = "test#$%";
        String encoded = soundex.encode(str);
        String decoded = soundex.decode(encoded);
        assertEquals(str, decoded);
    }

    /**
     * Test encoding string with accented characters.
     */
    @Test
    public void testAccentedCharacters() {
        Soundex soundex = new Soundex("US_ENGLISH_MAPPING_FOR_ACENTED_CHARACTERS");
        String str = "test é";
        String encoded = soundex.encode(str);
        String decoded = soundex.decode(encoded);
        assertEquals(str, decoded);
    }

    /**
     * Test encoding string with international characters.
     */
    @Test
    public void testInternationalCharacters() {
        Soundex soundex = new Soundex("INTERNATIONAL_MAPPING");
        String str = "test 你好";
        String encoded = soundex.encode(str);
        String decoded = soundex.decode(encoded);
        assertEquals(str, decoded);
    }

    /**
     * Test decoding encoded string with incorrect length.
     */
    @Test(expected = EncoderException.class)
    public void testDecodeIncorrectLength() {
        Soundex soundex = new Soundex();
        String encoded = "001";
        String decoded = soundex.decode(encoded);
    }

    /**
     * Test decoding encoded string with all zeros.
     */
    @Test
    public void testDecodeAllZeros() {
        Soundex soundex = new Soundex();
        String encoded = "0000";
        String decoded = soundex.decode(encoded);
        assertEquals("", decoded);
    }
}