package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test constructor Soundex().
     */
    @Test
    public void testSoundexInstance() {
        Soundex soundex = new Soundex();
        assertEquals("US_ENGLISH", soundex.getClass().getName());
        assertEquals(4, soundex.getMaxLength());
    }

    /**
     * Test constructor Soundex(char[]).
     */
    @Test
    public void testSoundexWithCustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(customMapping);
        assertEquals(customMapping.length, soundex.getSoundexMapping().length);
    }

    /**
     * Test constructor Soundex(String).
     */
    @Test
    public void testSoundexWithCustomMappingString() {
        String customMappingString = "1122334455";
        Soundex soundex = new Soundex(customMappingString);
        assertEquals(customMappingString.length(), soundex.getSoundexMapping().length);
    }

    /**
     * Test method difference.
     */
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int difference = soundex.difference(str1, str2);
        assertEquals(0, difference);

        str1 = "Banana";
        str2 = "Apple";
        difference = soundex.difference(str1, str2);
        assertEquals(1, difference);

        str1 = "Apple";
        str2 = "Apple";
        difference = soundex.difference(str1, str2);
        assertEquals(4, difference);

        str1 = "Apple";
        str2 = "AppleApple";
        difference = soundex.difference(str1, str2);
        assertEquals(4, difference);
    }

    /**
     * Test method encode.
     */
    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        String str = "Apple";
        String encoded = soundex.encode(str);
        assertEquals("A111", encoded);

        str = "Orange";
        encoded = soundex.encode(str);
        assertEquals("O211", encoded);

        str = "Banana";
        encoded = soundex.encode(str);
        assertEquals("B211", encoded);

        str = "AppleApple";
        encoded = soundex.encode(str);
        assertEquals("A111A111", encoded);
    }

    /**
     * Test method getMappingCode.
     */
    @Test
    public void testGetMappingCode() throws EncoderException {
        Soundex soundex = new Soundex();
        String testCases = "AppleAppleOrangeBananaApeApple";
        String str = "";

        for (char c : testCases.toCharArray()) {
            str += c;
            try {
                char code = soundex.getMappingCode(str, str.length() - 1);
                System.out.println("Character '" + c + "' has code '" + code + "'");
            } catch (IllegalArgumentException e) {
                System.out.println("Character '" + c + "' is not mapped: " + e.getMessage());
            }
        }
    }

    /**
     * Test method map.
     */
    @Test
    public void testMap() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();

        for (char c = 'A'; c <= 'Z'; c++) {
            try {
                char code = soundex.map(c);
                System.out.println("Character '" + c + "' has code '" + code + "'");
            } catch (IllegalArgumentException e) {
                System.out.println("Character '" + c + "' is not mapped: " + e.getMessage());
            }
        }
    }
}