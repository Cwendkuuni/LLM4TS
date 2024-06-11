package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;
import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test constructor Soundex().
     */
    @Test
    public void testSoundexConstructor() {
        Soundex soundexInstance = new Soundex();
        assertNotNull(soundexInstance);
        assertEquals(US_ENGLISH_MAPPING, soundexInstance.getSoundexMapping());
    }

    /**
     * Test constructor Soundex(char[]).
     */
    @Test
    public void testSoundexConstructorWithMapping() {
        char[] mapping = US_ENGLISH_MAPPING_STRING.toCharArray();
        Soundex soundexInstance = new Soundex(mapping);
        assertNotNull(soundexInstance);
        assertEquals(mapping, soundexInstance.getSoundexMapping());
    }

    /**
     * Test constructor Soundex(String).
     */
    @Test
    public void testSoundexConstructorWithMappingString() {
        Soundex soundexInstance = new Soundex(US_ENGLISH_MAPPING_STRING);
        assertNotNull(soundexInstance);
        assertEquals(US_ENGLISH_MAPPING, soundexInstance.getSoundexMapping());
    }

    /**
     * Test method difference.
     */
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundexInstance = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int result = soundexInstance.difference(str1, str2);
        assertEquals(0, result);

        str1 = "Banana";
        str2 = "Apple";
        result = soundexInstance.difference(str1, str2);
        assertEquals(2, result);

        str1 = "Banana";
        str2 = "Banana";
        result = soundexInstance.difference(str1, str2);
        assertEquals(4, result);
    }

    /**
     * Test method encode.
     */
    @Test
    public void testEncode() throws EncoderException {
        Soundex soundexInstance = new Soundex();
        String str = "Apple";
        String result = soundexInstance.encode(str);
        assertEquals("A230", result);

        str = "Orange";
        result = soundexInstance.encode(str);
        assertEquals("O162", result);

        str = "Banana";
        result = soundexInstance.encode(str);
        assertEquals("B251", result);

        str = "Kiwi";
        result = soundexInstance.encode(str);
        assertEquals("K154", result);
    }

    /**
     * Test method soundex.
     */
    @Test
    public void testSoundexMethod() {
        Soundex soundexInstance = new Soundex();
        String str = "Apple";
        String result = soundexInstance.soundex(str);
        assertEquals("A230", result);

        str = "Orange";
        result = soundexInstance.soundex(str);
        assertEquals("O162", result);

        str = "Banana";
        result = soundexInstance.soundex(str);
        assertEquals("B251", result);

        str = "Kiwi";
        result = soundexInstance.soundex(str);
        assertEquals("K154", result);
    }
}