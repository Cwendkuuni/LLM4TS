package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test class for SoundexUtils
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "A short string with lower case letters, digits and spaces";
        String expected = "A SHORT STRING WITH DIGITS AND SPACES";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedNullInput() {
        String es1 = null;
        String es2 = null;
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedEmptyInput() {
        String es1 = "";
        String es2 = "";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedSameInput() {
        String input = "Apple";
        String encoderType = "Soundex";
        StringEncoder encoder = null;
        String es1 = null;
        String es2 = null;

        try {
            encoder = new org.apache.commons.codec.language.soundex.Soundex();
            es1 = encoder.encode(input);
            es2 = es1;
            int expected = es1.length();
            int result = SoundexUtils.differenceEncoded(es1, es2);
            assertEquals(expected, result);
        } catch (EncoderException e) {
            fail("Error encoding input string: " + e.getMessage());
        }
    }

    @Test
    public void testDifferenceEncodedDifferentEncodedStrings() {
        String input1 = "Apple";
        String input2 = "Orange";
        String encoderType = "Soundex";
        StringEncoder encoder = null;
        String es1 = null;
        String es2 = null;

        try {
            encoder = new org.apache.commons.codec.language.soundex.Soundex();
            es1 = encoder.encode(input1);
            es2 = encoder.encode(input2);
            int expected = 0;
            int result = SoundexUtils.differenceEncoded(es1, es2);
            assertEquals(expected, result);
        } catch (EncoderException e) {
            fail("Error encoding input strings: " + e.getMessage());
        }

        // Hex encoding of Soundex encoded strings for comparison
        String hexEs1 = new String(Hex.encodeHex(es1.getBytes()));
        String hexEs2 = new String(Hex.encodeHex(es2.getBytes()));
        System.out.println("Soundex encoded strings: " + hexEs1 + " and " + hexEs2);
        String hexExpected = "41443133"; // Apple and Apple have the same Soundex encoding
        assertEquals(hexExpected, hexEs1);
        assertEquals(hexExpected, hexEs2);
    }

    @Test
    public void testDifferenceEncodedRefinedSoundexDifferentEncodedStrings() {
        String input1 = "Apple";
        String input2 = "Orange";
        String encoderType = "RefinedSoundex";
        StringEncoder encoder = null;
        String es1 = null;
        String es2 = null;

        try {
            encoder = new org.apache.commons.codec.language.soundex.Soundex(); // RefinedSoundex uses Soundex internally
            es1 = encoder.encode(input1);
            es2 = encoder.encode(input2);
            int expected = 3;
            int result = SoundexUtils.differenceEncoded(es1, es2);
            assertEquals(expected, result);
        } catch (EncoderException e) {
            fail("Error encoding input strings: " + e.getMessage());
        }

        // Hex encoding of RefinedSoundex encoded strings for comparison
        String hexEs1 = new String(Hex.encodeHex(es1.getBytes()));
        String hexEs2 = new String(Hex.encodeHex(es2.getBytes()));
        System.out.println("RefinedSoundex encoded strings: " + hexEs1 + " and " + hexEs2);
        String hexExpected = "414431332133"; // Apple and Orange have a RefinedSoundex difference of 3
        assertEquals(hexExpected, hexEs1);
        assertEquals(hexExpected, hexEs2);
    }
}