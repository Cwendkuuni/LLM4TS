package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.*;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test constructor Soundex().
     */
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        Assert.assertNotNull(soundex);
        String code = soundex.encode("test");
        assertEquals("test should have a soundex code", "T230", code);
    }

    /**
     * Test constructor Soundex(char[]).
     */
    @Test
    public void testSoundexWithCustomMapping() {
        char[] customMapping = {'0', '1', '1', '2', '1', '2', '2', '3', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'};
        Soundex soundex = new Soundex(customMapping);
        Assert.assertNotNull(soundex);
        String code = soundex.encode("test");
        assertNotEquals("test should have a different soundex code with custom mapping", "T230", code);
    }

    /**
     * Test constructor Soundex(String).
     */
    @Test
    public void testSoundexWithCustomMappingString() {
        Soundex soundex = new Soundex("0111212223111111111111111111");
        Assert.assertNotNull(soundex);
        String code = soundex.encode("test");
        assertNotEquals("test should have a different soundex code with custom mapping string", "T230", code);
    }

    /**
     * Test method difference.
     */
    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("test1", "test2");
        assertEquals("The difference between 'test1' and 'test2' should be 3", 3, difference);
        difference = soundex.difference("test1", "test1");
        assertEquals("The difference between the same string should be 4", 4, difference);
    }

    /**
     * Test method encode.
     */
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String code1 = soundex.encode("test");
        assertEquals("test should have a soundex code", "T230", code1);
        String code2 = soundex.encode("tesT");
        assertEquals("The soundex code should be case-insensitive", "T230", code2);
        String code3 = soundex.encode("");
        assertEquals("An empty string should return an empty string", "", code3);
        String code4 = soundex.encode(null);
        assertNull("A null input should return null", code4);

        try {
            soundex.encode("abcdefghijklmnopqrstuvwxyz");
            fail("Should throw an EncoderException for non-alphabetic characters");
        } catch (EncoderException e) {
            // Expected
        }
    }

    /**
     * Test method soundex.
     */
    @Test
    public void testSoundexWithMultipleWsAndHs() {
        Soundex soundex = new Soundex();
        String code = soundex.soundex("test HW test HW test HW test");
        assertEquals("The soundex code for 'test HW test HW test HW test' should be 'T230HW0HW0HW0'", "T230HW0HW0HW0", code);
    }

    /**
     * Test method soundex with custom mapping.
     */
    @Test
    public void testSoundexWithCustomMapping2() {
        char[] customMapping = {'0', '1', '1', '2', '1', '2', '2', '3', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'};
        Soundex soundex = new Soundex(customMapping);
        String code = soundex.soundex("test");
        assertNotEquals("test should have a different soundex code with custom mapping", "T230", code);
    }
}