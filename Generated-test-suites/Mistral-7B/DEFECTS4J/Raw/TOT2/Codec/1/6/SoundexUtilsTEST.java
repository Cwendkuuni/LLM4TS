package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.codec.language.SoundexUtils;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "aBc123DéFgHi";
        String expectedOutput = "ABCDEFGHI";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoding() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expectedOutput = 1;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithNullEncoding() throws EncoderException {
        String s1 = "Apple";
        String s2 = "Orange";
        SoundexUtils.difference(null, s1, s2);
    }

    @Test
    public void testDifferenceMethodWithEmptyStrings() {
        String es1 = "";
        String es2 = "";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithNullStrings() {
        String es1 = null;
        String es2 = null;
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithNullEncodedStrings() {
        String es1 = null;
        String es2 = "Apple";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);

        es1 = "Apple";
        int actualOutput2 = SoundexUtils.differenceEncoded(es1, null);
        assertEquals(expectedOutput, actualOutput2);
    }

    @Test
    public void testDifferenceMethodWithLongStrings() {
        String s1 = "Antidisestablishmentarianism";;
        String s2 = "Antidisestablishmentarianism";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.difference(new org.apache.commons.codec.language.Soundex(), s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }
}