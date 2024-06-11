package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testUSEnglishSoundex() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;

        // Test encoding of English words
        assertEquals("K150", soundex.encode("Kangaroo"));
        assertEquals("N136", soundex.encode("Apple"));
        assertEquals("T251", soundex.encode("Tomato"));
        assertEquals("S252", soundex.encode("Sunflower"));
        assertEquals("C423", soundex.encode("Cucumber"));

        // Test encoding of single letters
        assertEquals("K", soundex.encode("K"));
        assertEquals("N", soundex.encode("N"));
        assertEquals("T", soundex.encode("T"));
        assertEquals("S", soundex.encode("S"));
        assertEquals("C", soundex.encode("C"));

        // Test encoding with empty string
        assertEquals("", soundex.encode(""));

        // Test encoding with null string
        try {
            soundex.encode(null);
            fail("Expected EncoderException with message 'Parameter supplied to Soundex encode is not of type java.lang.String'");
        } catch (EncoderException e) {
            // Expected
        }

        // Test encoding with non-English characters
        assertEquals("X321", soundex.encode("Mango"));
        assertEquals("P155", soundex.encode("Pomelo"));
        assertEquals("Q162", soundex.encode("Quince"));

        // Test decoding of known soundex codes
        assertEquals("Kangaroo", soundex.decode("K150"));
        assertEquals("Apple", soundex.decode("N136"));
        assertEquals("Tomato", soundex.decode("T251"));
        assertEquals("Sunflower", soundex.decode("S252"));
        assertEquals("Cucumber", soundex.decode("C423"));
        assertEquals("Mango", soundex.decode("X321"));
        assertEquals("Pomelo", soundex.decode("P155"));
        assertEquals("Quince", soundex.decode("Q162"));

        // Test decoding of unknown soundex codes
        try {
            soundex.decode("X32Z");
            fail("Expected EncoderException with message 'No mapping for character 0x7A found'");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testCustomSoundexMapping() throws EncoderException {
        char[] customMapping = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Soundex customSoundex = new Soundex(customMapping);

        // Test encoding with English words using custom mapping
        assertEquals("K150", customSoundex.encode("Kangaroo"));
        assertEquals("N136", customSoundex.encode("Apple"));
        assertEquals("T251", customSoundex.encode("Tomato"));
        assertEquals("S252", customSoundex.encode("Sunflower"));
        assertEquals("C423", customSoundex.encode("Cucumber"));

        // Test encoding with single letters using custom mapping
        assertEquals("K", customSoundex.encode("K"));
        assertEquals("N", customSoundex.encode("N"));
        assertEquals("T", customSoundex.encode("T"));
        assertEquals("S", customSoundex.encode("S"));
        assertEquals("C", customSoundex.encode("C"));

        // Test encoding with empty string using custom mapping
        assertEquals("", customSoundex.encode(""));

        // Test encoding with null string using custom mapping
        try {
            customSoundex.encode(null);
            fail("Expected EncoderException with message 'Parameter supplied to Soundex encode is not of type java.lang.String'");
        } catch (EncoderException e) {
            // Expected
        }

        // Test encoding with non-English characters using custom mapping
        assertEquals("X321", customSoundex.encode("Mango"));
        assertEquals("P155", customSoundex.encode("Pomelo"));
        assertEquals("Q162", customSoundex.encode("Quince"));

        // Test decoding of known soundex codes using custom mapping
        assertEquals("Kangaroo", customSoundex.decode("K150"));
        assertEquals("Apple", customSoundex.decode("N136"));
        assertEquals("Tomato", customSoundex.decode("T251"));
        assertEquals("Sunflower", customSoundex.decode("S252"));
        assertEquals("Cucumber", customSoundex.decode("C423"));
        assertEquals("Mango", customSoundex.decode("X321"));
        assertEquals("Pomelo", customSoundex.decode("P155"));
        assertEquals("Quince", customSoundex.decode("Q162"));

        // Test decoding of unknown soundex codes using custom mapping
        try {
            customSoundex.decode("X32Z");
            fail("Expected EncoderException with message 'No mapping for character 0x7A found'");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;

        // Test difference between same strings
        assertEquals(4, soundex.difference("Apple", "Apple"));

        // Test difference between same strings in different cases
        assertEquals(4, soundex.difference("Apple", "apple"));

        // Test difference between similar strings
        assertEquals(3, soundex.difference("Apple", "Orange"));

        // Test difference between dissimilar strings
        assertEquals(0, soundex.difference("Apple", "Banana"));

        // Test difference between empty strings
        assertEquals(0, soundex.difference("", ""));

        // Test difference between null strings
        try {
            soundex.difference(null, "Apple");
            fail("Expected EncoderException with message 'Parameter supplied to Soundex difference (first argument) is null'");
        } catch (EncoderException e) {
            // Expected
        }

        // Test difference between strings with different lengths
        try {
            soundex.difference("Apple", "AppleApple");
            fail("Expected EncoderException with message 'The first argument to Soundex difference must have the same length as the second argument'");
        } catch (EncoderException e) {
            // Expected
        }
    }
}