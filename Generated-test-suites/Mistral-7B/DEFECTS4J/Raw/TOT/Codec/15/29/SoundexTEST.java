package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test for Soundex class
 */
public class SoundexTest {

    /**
     * Test for encode method with valid string
     */
    @Test
    public void testEncodeValidString() {
        String input = "Apple";
        String expected = "K230";
        String actual = new Soundex().encode(input);
        assertEquals(expected, actual);
    }

    /**
     * Test for encode method with empty string
     */
    @Test
    public void testEncodeEmptyString() {
        String actual = new Soundex().encode("");
        assertEquals("", actual);
    }

    /**
     * Test for encode method with null string
     */
    @Test(expected = EncoderException.class)
    public void testEncodeNullString() {
        new Soundex().encode(null);
    }

    /**
     * Test for encode method with internationalized mapping
     */
    @Test
    public void testEncodeInternationalizedMapping() {
        char[] mapping = {'1', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        Soundex soundex = new Soundex(mapping);
        String input = "Apple";
        String expected = "K230";
        String actual = soundex.encode(input);
        assertEquals(expected, actual);
    }

    /**
     * Test for difference method with identical strings
     */
    @Test
    public void testDifferenceIdenticalStrings() {
        String s1 = "Apple";
        String s2 = "Apple";
        int expected = 4;
        int actual = new Soundex().difference(s1, s2);
        assertEquals(expected, actual);
    }

    /**
     * Test for difference method with similar strings
     */
    @Test
    public void testDifferenceSimilarStrings() {
        String s1 = "Apple";
        String s2 = "Appel";
        int expected = 3;
        int actual = new Soundex().difference(s1, s2);
        assertEquals(expected, actual);
    }

    /**
     * Test for difference method with dissimilar strings
     */
    @Test
    public void testDifferenceDissimilarStrings() {
        String s1 = "Apple";
        String s2 = "Banana";
        int expected = 0;
        int actual = new Soundex().difference(s1, s2);
        assertEquals(expected, actual);
    }

    /**
     * Test for difference method with error scenario - null strings
     */
    @Test(expected = EncoderException.class)
    public void testDifferenceNullStrings() {
        String s1 = null;
        String s2 = "Apple";
        new Soundex().difference(s1, s2);
    }
}