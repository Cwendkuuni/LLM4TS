package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Test empty string
        assertEquals("", SoundexUtils.clean(""));

        // Test null string
        assertNull(SoundexUtils.clean(null));

        // Test lower case letters
        assertEquals("BAT", SoundexUtils.clean("bat"));
        assertEquals("CAT", SoundexUtils.clean("cat"));
        assertEquals("Dog", SoundexUtils.clean("dog"));

        // Test special characters
        assertEquals("Ada", SoundexUtils.clean("Ada!"));
        assertEquals("Ein", SoundexUtils.clean("Einstein"));
        assertEquals("Müller", SoundexUtils.clean("Müller"));

        // Test leading and trailing spaces
        assertEquals("BAT", SoundexUtils.clean("   Bat   "));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.Decoder();

        // Test empty strings
        assertEquals(4, SoundexUtils.difference(encoder, "", ""));

        // Test null strings
        assertNull(SoundexUtils.difference(encoder, null, ""));
        assertNull(SoundexUtils.difference(encoder, "", null));

        // Test same strings
        assertEquals(4, SoundexUtils.difference(encoder, "apple", "apple"));

        // Test different strings with no similarity
        assertEquals(0, SoundexUtils.difference(encoder, "apple", "banana"));

        // Test different strings with little similarity
        assertEquals(1, SoundexUtils.difference(encoder, "apple", "orange"));

        // Test different strings with strong similarity
        assertEquals(3, SoundexUtils.difference(encoder, "application", "applicator"));

        // Test different strings with identical values
        assertEquals(4, SoundexUtils.difference(encoder, "SoundexUtils", "SoundexUtils"));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsException() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.Decoder();

        // Test encoding an empty string throws an exception
        String emptyString = "";
        String encodedEmptyString = SoundexUtils.difference(encoder, emptyString, emptyString);
    }
}