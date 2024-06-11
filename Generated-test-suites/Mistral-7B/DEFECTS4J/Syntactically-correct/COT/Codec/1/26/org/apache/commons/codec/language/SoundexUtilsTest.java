package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    /**
     * Test method for {@link SoundexUtils#clean(String)}.
     */
    @Test
    public void testClean() {
        String input = "aBc 123!@#$%";
        String expected = "AB";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "";
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        assertNull(result);
    }

    /**
     * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}.
     */
    @Test
    public void testDifference() throws EncoderException {
        String encoderType = "Soundex";
        String encoder = new Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "applE";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple123";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple!@#$%^&*";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        encoderType = "RefinedSoundex";
        encoder = new RefinedSoundex();
        s1 = "Apple";
        s2 = "Orange";
        expected = 0;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 7;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "applE";
        expected = 7;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple123";
        expected = 7;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple!@#$%^&*";
        expected = 7;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)}.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "122311";
        String es2 = "111223";
        int expected = 3;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = null;
        es2 = "111223";
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);

        es1 = "122311";
        es2 = null;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);
    }
}