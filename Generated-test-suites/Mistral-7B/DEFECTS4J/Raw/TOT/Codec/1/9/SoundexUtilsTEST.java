package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test for the clean method.
     */
    @Test
    public void testClean() {
        String input = "The quick Brown fox jumps over the lazy Dog";
        String expected = "The quick Brown Fox jumps over the lazy Dog";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        assertNull(result);

        input = "";
        result = SoundexUtils.clean(input);
        assertEquals("", result);

        input = "12345";
        result = SoundexUtils.clean(input);
        assertEquals("", result);

        input = "AbCdEfGhIjKlMnOpQrStUvWxYz";
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "abCdEfGhIjKlMnOpQrStUvWxYz";
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for the difference method.
     */
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
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
        s2 = "Apple";
        s1 = encoder.encode(s1);
        s2 = encoder.encode(s2);
        expected = 4;
        result = SoundexUtils.differenceEncoded(s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Appel";
        expected = 3;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        s1 = encoder.encode(s1);
        s2 = encoder.encode(s2);
        expected = 4;
        result = SoundexUtils.differenceEncoded(s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        s1 = encoder.encode(s1);
        s2 = encoder.encode(s2);
        s1 = s1.substring(0, 3);
        s2 = s2.substring(0, 3);
        expected = 3;
        result = SoundexUtils.differenceEncoded(s1, s2);
        assertEquals(expected, result);
    }
}