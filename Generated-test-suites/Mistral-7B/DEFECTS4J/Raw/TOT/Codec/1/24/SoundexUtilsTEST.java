package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link SoundexUtils}.
 */
public class SoundexUtilsTest {

    /**
     * Test for {@link SoundexUtils#clean(String)} method.
     */
    @Test
    public void testClean() {
        String input = "The quick Brown Fox jumps over the lazy Dog";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method.
     */
    @Test
    public void testDifferenceEncoded() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        String es1 = encoder.encode(str1);
        String es2 = encoder.encode(str2);
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, difference);

        str1 = "Apple";
        str2 = "Apricot";
        es1 = encoder.encode(str1);
        es2 = encoder.encode(str2);
        difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(1, difference);

        str1 = "Apple";
        str2 = null;
        try {
            difference = SoundexUtils.differenceEncoded(es1, null);
        } catch (NullPointerException e) {
            difference = 0;
        }
        assertEquals(0, difference);
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)} method.
     */
    @Test
    public void testDifferenceWithEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int difference = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(0, difference);

        str1 = "Apple";
        str2 = "Apricot";
        difference = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(1, difference);

        str1 = "Apple";
        str2 = null;
        try {
            difference = SoundexUtils.difference(encoder, str1, null);
        } catch (NullPointerException e) {
            difference = 0;
        }
        assertEquals(0, difference);
    }
}