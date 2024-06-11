package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    /**
     * Test method for {@link SoundexUtils#clean(String)}.
     */
    @Test
    public void testClean() {
        StringUtils stringUtils = new StringUtils(); // Assuming StringUtils is available in your test class
        StringInputOutput inputOutput = new StringInputOutput(); // Assuming StringInputOutput is available in your test class

        // Testing null input
        String nullInput = null;
        String cleanNullInput = SoundexUtils.clean(nullInput);
        assertEquals("", cleanNullInput);

        // Testing empty input
        String emptyInput = "";
        String cleanEmptyInput = SoundexUtils.clean(emptyInput);
        assertEquals("", cleanEmptyInput);

        // Testing normal input
        String input = "Hello World!";
        String cleanInput = SoundexUtils.clean(input);
        String expectedCleanInput = "HELLO WORLD!".toUpperCase(java.util.Locale.ENGLISH);
        assertEquals(expectedCleanInput, cleanInput);

        // Testing input with only uppercase letters
        String uppercaseInput = "HELLO WORLD!".toUpperCase(java.util.Locale.ENGLISH);
        String cleanUppercaseInput = SoundexUtils.clean(uppercaseInput);
        assertEquals(expectedCleanInput, cleanUppercaseInput);

        // Testing input with digits
        String inputWithDigits = "Hello123 World!";
        String cleanInputWithDigits = SoundexUtils.clean(inputWithDigits);
        String expectedCleanInputWithDigits = "HELLO WORLD!".toUpperCase(java.util.Locale.ENGLISH);
        assertEquals(expectedCleanInputWithDigits, cleanInputWithDigits);

        // Testing input with special characters
        String inputWithSpecialCharacters = "Hello@World!";
        String cleanInputWithSpecialCharacters = SoundexUtils.clean(inputWithSpecialCharacters);
        String expectedCleanInputWithSpecialCharacters = "HELLO WORLD!".toUpperCase(java.util.Locale.ENGLISH);
        assertEquals(expectedCleanInputWithSpecialCharacters, cleanInputWithSpecialCharacters);
    }

    /**
     * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}.
     */
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new SoundexEncoder(); // Assuming SoundexEncoder is available in your test class
        StringInputOutput inputOutput = new StringInputOutput(); // Assuming StringInputOutput is available in your test class

        // Testing null inputs
        assertThrows(IllegalArgumentException.class, () -> SoundexUtils.difference(encoder, null, "Test"));
        assertThrows(IllegalArgumentException.class, () -> SoundexUtils.difference(encoder, "Test", null));

        // Testing empty inputs
        String emptyString1 = "";
        String emptyString2 = "";
        int differenceEmptyStrings = SoundexUtils.difference(encoder, emptyString1, emptyString2);
        assertEquals(0, differenceEmptyStrings);

        // Testing identical inputs
        String string1 = "Test";
        String string2 = "Test";
        int differenceIdenticalStrings = SoundexUtils.difference(encoder, string1, string2);
        int expectedDifferenceIdenticalStrings = 4;
        assertEquals(expectedDifferenceIdenticalStrings, differenceIdenticalStrings);

        // Testing different soundex codes
        String string1Different = "Apple";
        String string2Different = "Banana";
        int differenceDifferentStrings = SoundexUtils.difference(encoder, string1Different, string2Different);
        int expectedDifferenceDifferentStrings = 0;
        assertEquals(expectedDifferenceDifferentStrings, differenceDifferentStrings);
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)}.
     */
    @Test
    public void testDifferenceEncoded() {
        // Testing null inputs
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded(null, "Test"));
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded("Test", null));

        // Testing empty inputs
        String emptyString1 = "";
        String emptyString2 = "";
        int differenceEmptyStrings = SoundexUtils.differenceEncoded(emptyString1, emptyString2);
        assertEquals(0, differenceEmptyStrings);

        // Testing identical inputs
        String string1 = "Test";
        String string2 = "Test";
        String encodedString1 = new SoundexEncoder().encode(string1);
        String encodedString2 = new SoundexEncoder().encode(string2);
        int differenceIdenticalStrings = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
        int expectedDifferenceIdenticalStrings = 4;
        assertEquals(expectedDifferenceIdenticalStrings, differenceIdenticalStrings);

        // Testing different soundex codes
        String string1Different = "Apple";
        String string2Different = "Banana";
        String encodedString1Different = new SoundexEncoder().encode(string1Different);
        String encodedString2Different = new SoundexEncoder().encode(string2Different);
        int differenceDifferentStrings = SoundexUtils.differenceEncoded(encodedString1Different, encodedString2Different);
        int expectedDifferenceDifferentStrings = 0;
        assertEquals(expectedDifferenceDifferentStrings, differenceDifferentStrings);
    }
}