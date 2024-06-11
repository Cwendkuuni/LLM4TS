package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link SoundexUtils}.
 */
public class SoundexUtilsTest {

    /**
     * Test cleaning function.
     */
    @Test
    public void testClean() {
        String input = "A short input string with numbers, spaces and special characters: 123!@#$%^&*()_+={}[]|\\:;\"'<>,.?/~`";
        String expected = "A SHORT INPUT STRING WITH NUMBERS SPACES AND SPECIAL CHARACTERS: 123 !@#$%^&*()_+={}[]|\\:;\"'<>,./~`";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        String emptyInput = "";
        String resultEmpty = SoundexUtils.clean(emptyInput);
        assertEquals(emptyInput, resultEmpty);

        String nullInput = null;
        String resultNull = SoundexUtils.clean(nullInput);
        assertEquals(nullInput, resultNull);
    }

    /**
     * Test difference function with Soundex encoder.
     */
    @Test
    public void testDifferenceSoundex() throws EncoderException {
        String encoderName = "Soundex";
        StringEncoder encoder = null;
        try {
            encoder = org.apache.commons.codec.net.URLCodec.INSTANCE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);

        String s3 = "Apple";
        String s4 = "Apple";
        int expectedSame = 4;
        int actualSame = SoundexUtils.difference(encoder, s3, s4);
        assertEquals(expectedSame, actualSame);

        String s5 = "Apple";
        String s6 = "App";
        int expectedMinus = 1;
        int actualMinus = SoundexUtils.difference(encoder, s5, s6);
        assertEquals(expectedMinus, actualMinus);
    }

    /**
     * Test difference function with Refined Soundex encoder.
     */
    @Test
    public void testDifferenceRefinedSoundex() throws EncoderException {
        String encoderName = "RefinedSoundex";
        StringEncoder encoder = null;
        try {
            encoder = new org.apache.commons.codec.language.RefinedSoundex();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);

        String s3 = "Apple";
        String s4 = "Apple";
        int expectedSame = 4;
        int actualSame = SoundexUtils.difference(encoder, s3, s4);
        assertEquals(expectedSame, actualSame);

        String s5 = "Apple";
        String s6 = "App";
        int expectedMinus = 1;
        int actualMinus = SoundexUtils.difference(encoder, s5, s6);
        assertEquals(expectedMinus, actualMinus);

        String s7 = "Apple";
        String s8 = "Appleberry";
        int expectedPlus = 1;
        int actualPlus = SoundexUtils.difference(encoder, s7, s8);
        assertEquals(expectedPlus, actualPlus);
    }

    /**
     * Test differenceEncoded function.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "122312000";
        String es2 = "1200001223";
        int expected = 4;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }
}