package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test method for {@link Soundex#difference(String, String)}.
     */
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "apple";
        String str2 = "appal";
        int difference = soundex.difference(str1, str2);
        assertEquals(3, difference);
    }

    /**
     * Test method for {@link Soundex#soundex(String)}.
     */
    @Test
    public void testSoundex() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "apple";
        String expectedSoundex = "A111";
        String soundex1 = soundex.soundex(str1);
        assertEquals(expectedSoundex, soundex1);

        String str2 = "application";
        String expectedSoundex2 = "A120";
        String soundex2 = soundex.soundex(str2);
        assertEquals(expectedSoundex2, soundex2);

        String str3 = "antidisestablishmentarianism";
        String expectedSoundex3 = "A260";
        String soundex3 = soundex.soundex(str3);
        assertEquals(expectedSoundex3, soundex3);

        String str4 = "";
        String expectedSoundex4 = "";
        String soundex4 = soundex.soundex(str4);
        assertEquals(expectedSoundex4, soundex4);

        String str5 = null;
        try {
            soundex.soundex(str5);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }
}