package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test class for org.apache.commons.codec.language.Soundex
 */
public class SoundexTest {

    /**
     * Test encoding and decoding US_ENGLISH Soundex instance.
     */
    @Test
    public void testSoundexUSEnglish() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple, Banana, Cake, Dog, Elephant";
        String expected = "A123, B223, K113, D223, E133";

        String encoded = "";
        String decoded = "";

        String[] inputs = input.split(", ");
        for (String str : inputs) {
            try {
                encoded += soundex.encode(str) + " ";
                decoded += soundex.decode(soundex.encode(str)) + " ";
            } catch (EncoderException e) {
                fail("Failed to encode string: " + str);
            }
        }

        assertEquals(expected, encoded.trim());
        assertEquals(input, decoded.trim());
    }

    /**
     * Test encoding and decoding a custom Soundex instance.
     */
    @Test
    public void testSoundexCustomMapping() {
        String mapping = "0123011101255511012623010202";
        Soundex soundex = new Soundex(mapping.toCharArray());
        String input = "Apple, Banana, Cake, Dog, Elephant";
        String expected = "A123, B113, K113, D223, E133";

        String encoded = "";
        String decoded = "";

        String[] inputs = input.split(", ");
        for (String str : inputs) {
            try {
                encoded += soundex.encode(str) + " ";
                decoded += soundex.decode(soundex.encode(str)) + " ";
            } catch (EncoderException e) {
                fail("Failed to encode string: " + str);
            }
        }

        assertEquals(expected, encoded.trim());
        assertEquals(input, decoded.trim());
    }

    /**
     * Test encoding null input.
     */
    @Test(expected = EncoderException.class)
    public void testSoundexNullInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        soundex.encode(null);
    }

    /**
     * Test encoding empty string.
     */
    @Test
    public void testSoundexEmptyString() {
        Soundex soundex = Soundex.US_ENGLISH;
        String encoded = "";
        String decoded = "";

        try {
            encoded = soundex.encode("");
            decoded = soundex.decode(encoded);
        } catch (EncoderException e) {
            fail("Failed to encode empty string");
        }

        assertEquals("", encoded);
        assertEquals("", decoded);
    }
}