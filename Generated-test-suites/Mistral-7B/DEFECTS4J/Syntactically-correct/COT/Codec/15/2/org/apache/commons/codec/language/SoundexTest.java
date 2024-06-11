package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex.
 */
public class SoundexTest {

    private Soundex soundexUSEnglish = Soundex.US_ENGLISH;

    /**
     * Test method for {@link Soundex#difference(String, String)}.
     *
     * @throws EncoderException if an error occurs encoding one of the strings
     */
    @Test
    public void testDifference() throws EncoderException {
        String str1 = "Apple";
        String str2 = "Orange";
        int difference = soundexUSEnglish.difference(str1, str2);
        assertEquals(0, difference);

        str1 = "Kangaroo";
        str2 = "Elephant";
        difference = soundexUSEnglish.difference(str1, str2);
        assertEquals(0, difference);

        str1 = "Apple";
        str2 = "Application";
        difference = soundexUSEnglish.difference(str1, str2);
        assertEquals(3, difference);

        str1 = "Coding";
        str2 = "Coding";
        difference = soundexUSEnglish.difference(str1, str2);
        assertEquals(4, difference);

        str1 = "Apple";
        str2 = "Apple";
        difference = soundexUSEnglish.difference(str1, str2);
        assertEquals(4, difference);
    }

    /**
     * Test method for {@link Soundex#encode(String)}.
     */
    @Test
    public void testEncode() {
        Soundex soundexCustomMapping = new Soundex("43210120022455012623010202");

        String str1 = "Apple";
        String expectedResultUSEnglish = "A220";
        String resultUSEnglish = soundexUSEnglish.encode(str1);
        assertEquals(expectedResultUSEnglish, resultUSEnglish);

        String expectedResultCustomMapping = "A400";
        String resultCustomMapping = soundexCustomMapping.encode(str1);
        assertEquals(expectedResultCustomMapping, resultCustomMapping);

        String str2 = "Orange";
        String expectedResultUSEnglishForStr2 = "O152";
        String resultUSEnglishForStr2 = soundexUSEnglish.encode(str2);
        assertEquals(expectedResultUSEnglishForStr2, resultUSEnglishForStr2);

        String expectedResultCustomMappingForStr2 = "O420";
        String resultCustomMappingForStr2 = soundexCustomMapping.encode(str2);
        assertEquals(expectedResultCustomMappingForStr2, resultCustomMappingForStr2);

        String str3 = "Kangaroo";
        String expectedResultUSEnglishForStr3 = "K221";
        String resultUSEnglishForStr3 = soundexUSEnglish.encode(str3);
        assertEquals(expectedResultUSEnglishForStr3, resultUSEnglishForStr3);

        String expectedResultCustomMappingForStr3 = "K413";
        String resultCustomMappingForStr3 = soundexCustomMapping.encode(str3);
        assertEquals(expectedResultCustomMappingForStr3, resultCustomMappingForStr3);

        String str4 = "Elephant";
        String expectedResultUSEnglishForStr4 = "E131";
        String resultUSEnglishForStr4 = soundexUSEnglish.encode(str4);
        assertEquals(expectedResultUSEnglishForStr4, resultUSEnglishForStr4);

        String expectedResultCustomMappingForStr4 = "E311";
        String resultCustomMappingForStr4 = soundexCustomMapping.encode(str4);
        assertEquals(expectedResultCustomMappingForStr4, resultCustomMappingForStr4);

        String str5 = "AppleApple";
        String expectedResultUSEnglishForStr5 = "A220A220";
        String resultUSEnglishForStr5 = soundexUSEnglish.encode(str5);
        assertEquals(expectedResultUSEnglishForStr5, resultUSEnglishForStr5);

        String expectedResultCustomMappingForStr5 = "A400A400";
        String resultCustomMappingForStr5 = soundexCustomMapping.encode(str5);
        assertEquals(expectedResultCustomMappingForStr5, resultCustomMappingForStr5);

        String str6 = "AppleApplication";
        String expectedResultUSEnglishForStr6 = "A220A2200220";
        String resultUSEnglishForStr6 = soundexUSEnglish.encode(str6);
        assertEquals(expectedResultUSEnglishForStr6, resultUSEnglishForStr6);

        String expectedResultCustomMappingForStr6 = "A400A400400";
        String resultCustomMappingForStr6 = soundexCustomMapping.encode(str6);
        assertEquals(expectedResultCustomMappingForStr6, resultCustomMappingForStr6);

        String str7 = null;
        try {
            String resultUSEnglishForStr7 = soundexUSEnglish.encode(str7);
            fail("EncoderException expected");
        } catch (EncoderException e) {
            // Expected
        }

        try {
            String resultCustomMappingForStr7 = soundexCustomMapping.encode(str7);
            fail("EncoderException expected");
        } catch (EncoderException e) {
            // Expected
        }

        String str8 = "";
        String expectedResultUSEnglishForStr8 = "";
        String resultUSEnglishForStr8 = soundexUSEnglish.encode(str8);
        assertEquals(expectedResultUSEnglishForStr8, resultUSEnglishForStr8);

        String expectedResultCustomMappingForStr8 = "";
        String resultCustomMappingForStr8 = soundexCustomMapping.encode(str8);
        assertEquals(expectedResultCustomMappingForStr8, resultCustomMappingForStr8);
    }
}