package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test case for SoundexUtils class
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "A short string with numbers and symbols! 1A";
        String expected = "A SHORT STRING WITH NUMBERS AND SYMBOLS! 1";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEmptyStrings() {
        String result = SoundexUtils.difference(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceNullString() {
        String s1 = null;
        String s2 = "test";
        try {
            int result = SoundexUtils.difference(null, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testDifferenceNullEncoder() {
        String s1 = "test";
        String s2 = null;
        try {
            int result = SoundexUtils.difference(null, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testDifferenceSameStrings() {
        String s1 = "test";
        String s2 = "test";
        int result = SoundexUtils.difference(null, s1, s2);
        assertEquals(4, result);
    }

    @Test
    public void testDifferenceDifferentStrings() {
        String s1 = "test";
        String s2 = "tester";
        int result = SoundexUtils.difference(null, s1, s2);
        assertEquals(3, result);
    }

    @Test
    public void testDifferenceLongStrings() {
        String s1 = "This is a test string with a lot of characters in it.";
        String s2 = "This is a test string with a lot of characters in it.";
        int result = SoundexUtils.difference(null, s1, s2);
        assertEquals(4, result);
    }

    @Test
    public void testDifferenceLongerStringSoundex() {
        String s1 = "This is a test string with a lot of characters in it.";
        String s2 = "This is a test string with a lot of sounds like characters in it.";
        int result = SoundexUtils.difference(null, s1, s2);
        assertEquals(3, result);
    }

    @Test
    public void testDifferenceEmptySoundexEncodedStrings() {
        String es1 = "";
        String es2 = "";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceNullSoundexEncodedString() {
        String es1 = null;
        String es2 = "test";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceSoundexEncodedSameStrings() {
        String es1 = "123000";
        String es2 = "123000";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(4, result);
    }

    @Test
    public void testDifferenceSoundexEncodedDifferentStrings() {
        String es1 = "123000";
        String es2 = "123500";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(1, result);
    }

    @Test
    public void testDifferenceSoundexEncodedLongStrings() {
        String es1 = "1234567890123456789012345678901234567890123456789012345678901234";
        String es2 = "1234567890123456789012345678901234567890123456789012345678901234567890123";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(4, result);
    }

}