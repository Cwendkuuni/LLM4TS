package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

/**
 * Test class for SoundexUtils methods.
 */
public class SoundexUtilsTest {

    /**
     * Test method for {@link SoundexUtils#clean(String)}.
     */
    @Test
    public void testClean() {
        String input = "The quick brown fox jumps over the lazy dog";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        assertEquals(input, result);

        input = " ";
        result = SoundexUtils.clean(input);
        assertEquals(input, result);
    }

    /**
     * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}.
     */
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = org.apache.commons.codec.net.URLCodec.INSTANCE;
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Soundex";
        s2 = "Soundex";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apfel";
        expected = 3;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "banana";
        expected = 0;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = null;
        s2 = "Apple";
        try {
            SoundexUtils.difference(encoder, s1, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }

        s1 = "Apple";
        s2 = null;
        try {
            SoundexUtils.difference(encoder, s1, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)}.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "12345";
        String es2 = "54321";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "131313";
        es2 = "131313";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "131313";
        es2 = "133113";
        expected = 3;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = null;
        es2 = "131313";
        try {
            SoundexUtils.differenceEncoded(es1, es2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        es1 = "131313";
        es2 = null;
        try {
            SoundexUtils.differenceEncoded(es1, es2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}