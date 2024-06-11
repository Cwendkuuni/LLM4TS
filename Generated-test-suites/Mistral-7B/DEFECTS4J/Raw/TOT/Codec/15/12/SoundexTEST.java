package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test method for {@link Soundex#difference(String, String)} with same string.
     */
    @Test
    public void testDifferenceSameString() {
        Soundex soundex = new Soundex();
        String str1 = "apple";
        String str2 = str1;
        int difference = 0;
        try {
            difference = soundex.difference(str1, str2);
        } catch (EncoderException e) {
            e.printStackTrace();
            fail("EncoderException was thrown when it should not be.");
        }
        assertEquals(4, difference);
    }

    /**
     * Test method for {@link Soundex#difference(String, String)} with different strings having same soundex code.
     */
    @Test
    public void testDifferenceSameSoundexCode() {
        Soundex soundex = new Soundex();
        String str1 = "apple";
        String str2 = "appel";
        int difference = 0;
        try {
            difference = soundex.difference(str1, str2);
        } catch (EncoderException e) {
            e.printStackTrace();
            fail("EncoderException was thrown when it should not be.");
        }
        assertEquals(4, difference);
    }

    /**
     * Test method for {@link Soundex#difference(String, String)} with different strings having different soundex codes.
     */
    @Test
    public void testDifferenceDifferentSoundexCode() {
        Soundex soundex = new Soundex();
        String str1 = "apple";
        String str2 = "banana";
        int difference = 0;
        try {
            difference = soundex.difference(str1, str2);
        } catch (EncoderException e) {
            e.printStackTrace();
            fail("EncoderException was thrown when it should not be.");
        }
        assertEquals(0, difference);
    }

    /**
     * Test method for {@link Soundex#encode(String)} with empty string.
     */
    @Test
    public void testEncodeEmptyString() {
        Soundex soundex = new Soundex();
        String encoded = "";
        try {
            encoded = soundex.encode(null);
        } catch (EncoderException e) {
            e.printStackTrace();
            fail("EncoderException was thrown when it should not be.");
        }
        assertEquals("", encoded);

        try {
            encoded = soundex.encode("");
        } catch (EncoderException e) {
            e.printStackTrace();
            fail("EncoderException was thrown when it should not be.");
        }
        assertEquals("", encoded);
    }

    /**
     * Test method for {@link Soundex#encode(String)} with null string.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeNullString() {
        Soundex soundex = new Soundex();
        String encoded = null;
        try {
            encoded = soundex.encode(null);
        } catch (EncoderException e) {
            //expected
        }
        assertNull(encoded);
    }

    /**
     * Test method for {@link Soundex#encode(String)} with valid string.
     */
    @Test
    public void testEncodeValidString() {
        Soundex soundex = new Soundex();
        String str = "apple";
        String encoded = "";
        try {
            encoded = soundex.encode(str);
        } catch (EncoderException e) {
            e.printStackTrace();
            fail("EncoderException was thrown when it should not be.");
        }
        assertEquals("A123", encoded);
    }

    /**
     * Test method for {@link Soundex#encode(String)} with valid edge cases.
     */
    @Test
    public void testEncodeEdgeCases() {
        Soundex soundex = new Soundex();
        String str = "Einstein";
        String encoded = "";
        try {
            encoded = soundex.encode(str);
        } catch (EncoderException e) {
            e.printStackTrace();
            fail("EncoderException was thrown when it should not be.");
        }
        assertEquals("E111", encoded);

        str = "Schrödinger";
        encoded = "";
        try {
            encoded = soundex.encode(str);
        } catch (EncoderException e) {
            e.printStackTrace();
            fail("EncoderException was thrown when it should not be.");
        }
        assertEquals("S3211", encoded);
    }
}