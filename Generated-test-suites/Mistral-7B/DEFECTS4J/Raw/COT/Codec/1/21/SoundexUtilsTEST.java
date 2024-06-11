package org.apache.commons.codec.language.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.encoding.ASCII;

/**
 * Test class for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test method for {@link SoundexUtils#clean(String)}.
     */
    @org.junit.Test
    public void testClean() {
        String input = "aBc123dEf!@#";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "";
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}.
     */
    @org.junit.Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new ASCII();
        String str1 = "Apple";
        String str2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);

        str1 = "Apple";
        str2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);

        str1 = "Apple";
        str2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);

        str1 = null;
        String str3 = "Apple";
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, str1, str3));

        str1 = "Apple";
        String str4 = null;
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, str1, str4));

        str1 = "Apple";
        str2 = "AppleApple";
        expected = 4;
        result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);

        str1 = "Apple";
        str2 = "AppleAppleApple";
        expected = 4;
        result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);

        str1 = "Apple";
        str2 = "AppleAppleAppleApple";
        expected = 4;
        result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)}.
     */
    @org.junit.Test
    public void testDifferenceEncoded() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "A123";
        es2 = "A123";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "A123";
        es2 = "A123A123";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "A123";
        es2 = "A123A123A123";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }
}