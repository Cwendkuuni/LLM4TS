package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test encoding of single English letter.
     */
    @Test
    public void testEncodeSingleLetter() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "A000";
        String actual;

        // Test capital English letter
        actual = soundex.encode("A");
        assertEquals(expected, actual);

        // Test lowercase English letter
        expected = "A000";
        actual = soundex.encode("a");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding of multiple English letters.
     */
    @Test
    public void testEncodeMultipleLetters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "A123";
        String actual;

        // Test multiple capital English letters
        actual = soundex.encode("Apple");
        assertEquals(expected, actual);

        // Test multiple lowercase English letters
        expected = "A123";
        actual = soundex.encode("apple");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding of empty string.
     */
    @Test
    public void testEncodeEmptyString() {
        Soundex soundex = Soundex.US_ENGLISH;
        String actual = soundex.encode("");
        assertEquals("", actual);
    }

    /**
     * Test encoding of null string.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeNullString() {
        Soundex soundex = Soundex.US_ENGLISH;
        soundex.encode(null);
    }

    /**
     * Test encoding with HW rule.
     */
    @Test
    public void testEncodeWithHWRule() {
        Soundex soundex = new Soundex("01230120022455012623010202");
        String expected = "A223";
        String actual;

        // Test multiple capital English letters with H and W
        actual = soundex.encode("AppleW");
        assertEquals(expected, actual);

        // Test multiple lowercase English letters with H and W
        expected = "a223";
        actual = soundex.encode("applew");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding with non-English letters.
     */
    @Test
    public void testEncodeWithNonEnglishLetters() {
        Soundex soundex = new Soundex("01230120022455012623010202");
        String expected = "X000"; // Different soundex code for non-English letters
        String actual;

        // Test multiple capital non-English letters
        actual = soundex.encode("Γαβδ");
        assertEquals(expected, actual);

        // Test multiple lowercase non-English letters
        expected = "x000";
        actual = soundex.encode("γαβδ");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding with digits and special characters.
     */
    @Test
    public void testEncodeWithDigitsAndSpecialCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "0000";
        String actual;

        // Test string with digits
        actual = soundex.encode("12345");
        assertEquals(expected, actual);

        // Test string with special characters
        expected = "0000";
        actual = soundex.encode("!@#$%^&*()_+-=[]{}|\\:;\"'<>,./?");
        assertEquals(expected, actual);

        // Test string with digits and special characters
        expected = "0000";
        actual = soundex.encode("A1B!2C@3D#4E%5F");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding with empty string and null object.
     */
    @Test
    public void testEncodeWithEmptyStringAndNullObject() {
        Soundex soundex = Soundex.US_ENGLISH;
        String actual;

        // Test encoding with empty string
        actual = soundex.encode("");
        assertEquals("", actual);

        // Test encoding with null object
        try {
            actual = soundex.encode(null);
            fail();
        } catch (EncoderException e) {
            // Expected EncoderException
        }
    }

    /**
     * Test difference method.
     */
    @Test
    public void testDifference() {
        Soundex soundex = Soundex.US_ENGLISH;
        int difference;

        // Test difference between identical strings
        difference = soundex.difference("Apple", "Apple");
        assertEquals(4, difference);

        // Test difference between different strings with common prefix
        difference = soundex.difference("Apple", "Apricot");
        assertEquals(2, difference);

        // Test difference between different strings with no common prefix
        difference = soundex.difference("Apple", "Banana");
        assertEquals(0, difference);

        // Test difference between empty strings
        difference = soundex.difference("", "");
        assertEquals(0, difference);
    }

    /**
     * Test encode method from Encoder interface.
     */
    @Test
    public void testEncodeFromEncoderInterface() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        String encoded;

        // Test encoding of single English letter
        encoded = (String) soundex.encode("A");
        assertEquals("A000", encoded);

        // Test encoding of multiple English letters
        encoded = (String) soundex.encode("Apple");
        assertEquals("A123", encoded);
    }
}