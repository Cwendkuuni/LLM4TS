package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() {
        Soundex soundex = Soundex.US_ENGLISH;

        String str1 = "apple";
        String str2 = "appel";
        int difference = soundex.difference(str1, str2);
        assertEquals(2, difference);

        str1 = "abcdefghijklmnopqrstuvwxyz";
        str2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        difference = soundex.difference(str1, str2);
        assertEquals(0, difference);

        str1 = "apple";
        str2 = "apfel";
        difference = soundex.difference(str1, str2);
        assertEquals(3, difference);

        str1 = "apple";
        str2 = "aple";
        difference = soundex.difference(str1, str2);
        assertEquals(1, difference);

        str1 = "apple";
        str2 = "";
        difference = soundex.difference(str1, str2);
        assertNull(str2);
    }

    @Test
    public void testEncode() {
        Soundex soundex = Soundex.US_ENGLISH;

        String str = "apple";
        String encoded = soundex.encode(str);
        assertEquals("A123", encoded);

        str = "Apple";
        encoded = soundex.encode(str);
        assertEquals("A123", encoded);

        str = "apfel";
        encoded = soundex.encode(str);
        assertEquals("A113", encoded);

        str = "";
        encoded = soundex.encode(str);
        assertNull(encoded);

        str = null;
        try {
            encoded = soundex.encode(str);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncodeWithCustomMapping() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING_STRING.substring(0, 5) + "12345");

        String str = "apple";
        String encoded = soundex.encode(str);
        assertEquals("A113", encoded);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = Soundex.US_ENGLISH;

        String str = "apple";
        String soundexCode = soundex.soundex(str);
        assertEquals("A123", soundexCode);

        str = "Apple";
        soundexCode = soundex.soundex(str);
        assertEquals("A123", soundexCode);

        str = "apfel";
        soundexCode = soundex.soundex(str);
        assertEquals("A113", soundexCode);

        str = "";
        soundexCode = soundex.soundex(str);
        assertNull(soundexCode);

        str = null;
        try {
            soundexCode = soundex.soundex(str);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }
}