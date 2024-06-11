package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    /**
     * Test method for {@link SoundexUtils#clean(String)}.
     */
    @org.junit.Test
    public void testClean() {
        String input = "aBc 123!@#$%";
        String expected = "ABC";
        String actual = SoundexUtils.clean(input);
        assertEquals(expected, actual);

        // Edge case with empty string
        String emptyString = "";
        String emptyStringClean = SoundexUtils.clean(emptyString);
        assertEquals(emptyString, emptyStringClean);
    }

    /**
     * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}.
     */
    @org.junit.Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String input1 = "Apple";
        String input2 = "Orange";
        int expected = 1;
        int actual = SoundexUtils.difference(encoder, input1, input2);
        assertEquals(expected, actual);

        String input3 = "Apple";
        String input4 = "Apple";
        int expectedMax = 4;
        int actualMax = SoundexUtils.difference(encoder, input3, input4);
        assertEquals(expectedMax, actualMax);

        String input5 = null;
        String input6 = "Apple";
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, input5, input6));
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)}.
     */
    @org.junit.Test
    public void testDifferenceEncoded() {
        String input1 = "A0B0C0";
        String input2 = "A0B1C0";
        int expected = 2;
        int actual = SoundexUtils.differenceEncoded(input1, input2);
        assertEquals(expected, actual);

        String input3 = null;
        String input4 = "A0B0C0";
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded(input3, input4));
    }
}