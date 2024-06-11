package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    /**
     * Test for clean method.
     */
    @Test
    public void testClean() {
        String input = "aBc123DEfG!@#$%";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        assertNull(result);

        input = "";
        result = SoundexUtils.clean(input);
        assertEquals("", result);
    }

    /**
     * Test for difference method with Soundex encoder.
     */
    @Test
    public void testDifferenceSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.soundex.SoundexEncoder();
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
        s2 = "Apples";
        expected = 3;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = null;
        String s3 = "Apple";
        result = SoundexUtils.difference(encoder, s1, s3);
        assertNull(result);

        s1 = "Apple";
        String s4 = null;
        result = SoundexUtils.difference(encoder, s1, s4);
        assertNull(result);
    }

    /**
     * Test for differenceEncoded method.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "A1B";
        es2 = "A1B";
        expected = 2;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = null;
        String es3 = "A1B";
        result = SoundexUtils.differenceEncoded(es1, es3);
        assertNull(result);

        es1 = "A1B";
        es3 = null;
        result = SoundexUtils.differenceEncoded(es1, es3);
        assertNull(result);
    }

}