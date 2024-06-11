package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Soundex.
 */
public class SoundexTest {

    /**
     * Test for encode method with US_ENGLISH mapping.
     */
    @Test
    public void testEncodeWithUSEnglish() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple";
        String expected = "A221";

        try {
            String result = soundex.encode(input);
            Assert.assertEquals(expected, result);
        } catch (EncoderException e) {
            // EncoderException should not be thrown for encode method
            Assert.fail("Unexpected EncoderException");
        }
    }

    /**
     * Test for encode method with custom mapping.
     */
    @Test
    public void testEncodeWithCustomMapping() {
        final char[] customMapping = {'0', '1', '2', '2', '3', '3', '4', '4', '5', '5', '6', '6', '7', '7', '8', '8', '9', '9'};
        Soundex soundex = new Soundex(customMapping);
        String input = "Apple";
        String expected = "A22"; // Custom mapping may produce different results

        try {
            String result = soundex.encode(input);
            Assert.assertEquals(expected, result);
        } catch (EncoderException e) {
            // EncoderException should not be thrown for encode method
            Assert.fail("Unexpected EncoderException");
        }
    }

    /**
     * Test for difference method with identical strings.
     */
    @Test
    public void testDifferenceWithIdenticalStrings() {
        Soundex soundex = Soundex.US_ENGLISH;
        String str1 = "Apple";
        String str2 = "Apple";

        int difference = 0;
        try {
            difference = soundex.difference(str1, str2);
        } catch (EncoderException e) {
            // EncoderException should not be thrown for difference method
            Assert.fail("Unexpected EncoderException");
        }

        Assert.assertEquals(4, difference);
    }

    /**
     * Test for difference method with different strings having one character difference.
     */
    @Test
    public void testDifferenceWithOneCharacterDifference() {
        Soundex soundex = Soundex.US_ENGLISH;
        String str1 = "Apple";
        String str2 = "Orange";

        int difference = 0;
        try {
            difference = soundex.difference(str1, str2);
        } catch (EncoderException e) {
            // EncoderException should not be thrown for difference method
            Assert.fail("Unexpected EncoderException");
        }

        Assert.assertEquals(1, difference);
    }

    /**
     * Test for difference method with empty strings.
     */
    @Test
    public void testDifferenceWithEmptyStrings() {
        Soundex soundex = Soundex.US_ENGLISH;
        String str1 = "";
        String str2 = "";

        int difference = 0;
        try {
            difference = soundex.difference(str1, str2);
        } catch (EncoderException e) {
            // EncoderException should not be thrown for difference method
            Assert.fail("Unexpected EncoderException");
        }

        Assert.assertEquals(4, difference);
    }

    /**
     * Test for encode method with null input.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeWithNullInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = null;

        soundex.encode(input);
    }

    /**
     * Test for encode method with empty string input.
     */
    @Test
    public void testEncodeWithEmptyStringInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "";

        String result = soundex.encode(input);
        Assert.assertEquals("", result);
    }

    /**
     * Test for difference method with null input.
     */
    @Test(expected = EncoderException.class)
    public void testDifferenceWithNullInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        String str1 = null;
        String str2 = "Apple";

        int difference = 0;
        try {
            difference = soundex.difference(str1, str2);
        } catch (EncoderException e) {
            // EncoderException is expected
            return;
        }

        Assert.fail("EncoderException should have been thrown");
    }

    /**
     * Test for difference method with null and empty string inputs.
     */
    @Test
    public void testDifferenceWithNullAndEmptyStringInputs() {
        Soundex soundex = Soundex.US_ENGLISH;
        String str1 = null;
        String str2 = "";

        int difference = 0;
        try {
            difference = soundex.difference(str1, str2);
        } catch (EncoderException e) {
            // EncoderException should have been thrown for str1
            return;
        }

        Assert.assertEquals(4, difference);
    }
}