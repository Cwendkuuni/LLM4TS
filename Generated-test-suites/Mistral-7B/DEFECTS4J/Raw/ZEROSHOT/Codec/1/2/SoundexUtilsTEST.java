package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

import org.junit.Test;

/**
 * Test class for SoundexUtils methods.
 */
public class SoundexUtilsTest {

    /**
     * Test for SoundexUtils.clean() method.
     */
    @Test
    public void testClean() {
        String input = null;
        String expected = null;
        String result;

        // Test case 1: empty input
        input = "";
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        // Test case 2: null input
        input = null;
        expected = null;
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        // Test case 3: clean a string
        input = "abCdEfGhIjKlMnOpQrStUvWxYz";
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        // Test case 4: clean a string with only one character
        input = "a";
        expected = "A";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        // Test case 5: clean a string with no letters
        input = "12345";
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for SoundexUtils.difference() method.
     */
    @Test
    public void testDifference() throws EncoderException {
        String encoderName = "Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        String s3 = "Apple";
        String s4 = "Apple";
        String s5 = "Apple";
        String es1, es2, es3, es4, es5;

        // Test case 1: null encoder
        StringEncoder encoder = null;
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        // Test case 2: initialize encoder
        encoder = new org.apache.commons.codec.language.Soundex();

        // Test case 3: encode strings
        es1 = encoder.encode(s1);
        es2 = encoder.encode(s2);
        es3 = encoder.encode(s3);
        es4 = encoder.encode(s4);
        es5 = encoder.encode(s5);

        // Test case 4: difference with different strings
        int diff = SoundexUtils.difference(encoder, s1, s2);
        int expectedDiff = 0;
        assertEquals(expectedDiff, diff);

        // Test case 5: difference with identical strings
        diff = SoundexUtils.difference(encoder, s1, s3);
        int expectedDiffIdentical = 4;
        assertEquals(expectedDiffIdentical, diff);

        // Test case 6: difference with identical strings (same encoder instance)
        diff = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedDiffIdentical, diff);

        // Test case 7: difference with identical strings (different encoder instances)
        StringEncoder encoder2 = new org.apache.commons.codec.language.Soundex();
        es1 = encoder.encode(s1);
        es2 = encoder2.encode(s1);
        diff = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedDiffIdentical, diff);

        // Test case 8: difference with similar strings
        s2 = "Apricot";
        diff = SoundexUtils.difference(encoder, s1, s2);
        int expectedDiffSimilar = 3;
        assertEquals(expectedDiffSimilar, diff);

        // Test case 9: difference with similar strings (same encoder instance)
        diff = SoundexUtils.differenceEncoded(es1, es3);
        assertEquals(expectedDiffIdentical, diff);

        // Test case 10: difference with similar strings (different encoder instances)
        StringEncoder encoder3 = new org.apache.commons.codec.language.Soundex();
        es1 = encoder.encode(s1);
        es3 = encoder3.encode(s1);
        diff = SoundexUtils.differenceEncoded(es1, es3);
        assertEquals(expectedDiffIdentical, diff);

        // Test case 11: difference with strings having the same characters but
        // different orders
        s2 = "Elephant";
        diff = SoundexUtils.difference(encoder, s1, s2);
        int expectedDiffDifferentOrder = 3;
        assertEquals(expectedDiffDifferentOrder, diff);

        // Test case 12: difference with strings having the same characters but
        // different orders (same encoder instance)
        diff = SoundexUtils.differenceEncoded(es1, es5);
        assertEquals(expectedDiffDifferentOrder, diff);

        // Test case 13: difference with strings having the same characters but
        // different orders (different encoder instances)
        StringEncoder encoder4 = new org.apache.commons.codec.language.Soundex();
        es1 = encoder.encode(s1);
        es5 = encoder4.encode(s5);
        diff = SoundexUtils.differenceEncoded(es1, es5);
        assertEquals(expectedDiffDifferentOrder, diff);
    }
}