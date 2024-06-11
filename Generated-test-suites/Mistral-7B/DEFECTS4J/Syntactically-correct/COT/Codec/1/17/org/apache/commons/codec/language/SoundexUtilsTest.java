package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "A an Apple a day keeps the Doctor away";
        String expected = "A A A A A A A A A A";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "123A an Apple a day keeps the Doctor away";
        expected = "A";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        assertEquals(emptyString(), result);

        input = "";
        result = SoundexUtils.clean(input);
        assertEquals(emptyString(), result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithNullEncoder() {
        String s1 = "apple";
        String s2 = "orange";
        SoundexUtils.difference(null, s1, s2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithNullString1() {
        StringEncoder encoder = (StringEncoder) () -> "";
        String s2 = "orange";
        SoundexUtils.difference(encoder, null, s2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithNullString2() {
        StringEncoder encoder = (StringEncoder) () -> "soundex";
        String s1 = "apple";
        SoundexUtils.difference(encoder, s1, null);
    }

    @Test
    public void testDifferenceMethod() {
        StringEncoder encoder = (StringEncoder) () -> "soundex";
        String s1 = "apple";
        String s2 = "appel";
        int expected = 3;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s2 = "application";
        expected = 2;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "orange";
        s2 = "appel";
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, result);

        s1 = "apple";
        s2 = "apfel";
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(2, result);

        s1 = "apple";
        s2 = "Apple";
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(1, result);
    }

    @Test
    public void testDifferenceEncodedMethod() {
        String es1 = "112110";
        String es2 = "112100";
        int expected = 1;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "112110";
        es2 = "112210";
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);

        es1 = "112110";
        es2 = "112120";
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);
    }

    private String emptyString() {
        return "";
    }
}