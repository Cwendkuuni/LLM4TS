package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Soundex encoder.
 */
public class SoundexTest {

    /**
     * Test encoding for US_ENGLISH mapping.
     */
    @Test
    public void testUSEnglishEncoding() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple, Application, Approach, April, Applebees";
        String[] expected = {"P133", "P165", "A260", "A154", "P133"};
        String[] actual = new String[5];
        for (int i = 0; i < 5; i++) {
            actual[i] = soundex.encode(input.split(",")[i]);
        }
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test encoding for custom mapping.
     */
    @Test
    public void testCustomMappingEncoding() {
        char[] customMapping = {'0', '0', '0', '0', '1', '2', '2', '3', '3', '4', '5', '5', '6', '6', '7', '7', '8', '8'};
        Soundex soundex = new Soundex(customMapping);
        String input = "Alpha, Bravo, Charlie, Delta, Echo";
        String[] expected = {"120", "120", "330", "330", "550"};
        String[] actual = new String[5];
        for (int i = 0; i < 5; i++) {
            actual[i] = soundex.encode(input.split(",")[i]);
        }
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test encoding for empty string and null string.
     */
    @Test(expected = EncoderException.class)
    public void testEncodingEmptyAndNullString() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        soundex.encode("");
        soundex.encode(null);
    }

    /**
     * Test encoding for strings with special characters.
     */
    @Test
    public void testEncodingStringsWithSpecialCharacters() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        String input = "Apple!, Application#, Approach$, April%, Applebees&";
        String[] expected = {"P133", "P165", "A260", "A154", "P133"};
        String[] actual = new String[5];
        for (int i = 0; i < 5; i++) {
            actual[i] = soundex.encode(input.split(",")[i]);
        }
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test encoding for strings with multiple words.
     */
    @Test
    public void testEncodingStringsWithMultipleWords() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        String input = "Apple Application Approach April Applebees";
        String[] expected = {"P133", "P165", "A260", "A154", "P133"};
        String[] actual = new String[5];
        for (int i = 0; i < 5; i++) {
            actual[i] = soundex.encode(input.split(" ")[i]);
        }
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test encoding for strings with duplicate characters.
     */
    @Test
    public void testEncodingStringsWithDuplicateCharacters() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        String input = "AppleAppleApple";
        String expected = "P133";
        String actual = soundex.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encoding for strings with digits.
     */
    @Test
    public void testEncodingStringsWithDigits() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        String input = "Apple1Apple2Apple3";
        String[] expected = {"P133", "P133", "P133"};
        String[] actual = new String[3];
        for (int i = 0; i < 3; i++) {
            actual[i] = soundex.encode(input.split(" ")[i]);
        }
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test encoding for strings with empty space at the end.
     */
    @Test
    public void testEncodingStringsWithEmptySpaceAtTheEnd() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        String input = "Apple Application Approach April Applebees ";
        String[] expected = {"P133", "P165", "A260", "A154", "P133"};
        String[] actual = new String[5];
        for (int i = 0; i < 5; i++) {
            actual[i] = soundex.encode(input.split(" ")[i]);
        }
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test encoding for strings with leading and trailing whitespaces.
     */
    @Test
    public void testEncodingStringsWithLeadingAndTrailingWhitespaces() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        String input = "   Apple   Application   Approach   April   Applebees   ";
        String[] expected = {"P133", "P165", "A260", "A154", "P133"};
        String[] actual = new String[5];
        for (int i = 0; i < 5; i++) {
            actual[i] = soundex.encode(input.split("\\s+")[i]);
        }
        Assert.assertArrayEquals(expected, actual);
    }
}