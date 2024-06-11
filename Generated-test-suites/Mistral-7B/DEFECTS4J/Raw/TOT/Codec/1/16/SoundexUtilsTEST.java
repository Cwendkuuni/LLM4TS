package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link SoundexUtils}.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String input = "Hello World!";
        String expectedOutput = "HELLO";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        // Edge case: empty input
        String emptyInput = "";
        String emptyOutput = "";
        String actualEmptyOutput = SoundexUtils.clean(emptyInput);
        assertEquals(emptyOutput, actualEmptyOutput);

        // Error case: null input
        String nullInput = null;
        String actualNullOutput = SoundexUtils.clean(nullInput);
        assertNull(actualNullOutput);
    }

    @Test
    public void testDifference() throws EncoderException {
        // Typical use case
        String encoderName = "Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.difference(new org.apache.commons.codec.soundex.Soundex(), s1, s2);
        assertEquals(expectedOutput, actualOutput);

        // Edge case: identical strings
        String identicalStrings = "Apple";
        String identicalStrings2 = "Apple";
        int identicalOutput = 4;
        int actualIdenticalOutput = SoundexUtils.difference(new org.apache.commons.codec.soundex.Soundex(), identicalStrings, identicalStrings2);
        assertEquals(identicalOutput, actualIdenticalOutput);

        // Error case: null input
        String nullString1 = null;
        String nullString2 = null;
        try {
            SoundexUtils.difference(new org.apache.commons.codec.soundex.Soundex(), nullString1, nullString2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case
        String encodedString1 = "1221312";
        String encodedString2 = "1131211";
        int expectedOutput = 2;
        int actualOutput = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
        assertEquals(expectedOutput, actualOutput);

        // Edge case: identical encoded strings
        String identicalEncodedStrings = "1221312";
        String identicalEncodedStrings2 = "1221312";
        int identicalOutput = 4;
        int actualIdenticalOutput = SoundexUtils.differenceEncoded(identicalEncodedStrings, identicalEncodedStrings2);
        assertEquals(identicalOutput, actualIdenticalOutput);

        // Error case: null input
        String nullEncodedString1 = null;
        String nullEncodedString2 = null;
        int actualNullOutput = SoundexUtils.differenceEncoded(nullEncodedString1, nullEncodedString2);
        assertEquals(0, actualNullOutput);
    }
}