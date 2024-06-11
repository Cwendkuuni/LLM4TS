package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        // Test with null string
        assertEquals(null, SoundexUtils.clean(null));

        // Test with empty string
        assertEquals("", SoundexUtils.clean(""));

        // Test with string contains non-letter characters
        assertEquals("", SoundexUtils.clean("123456"));

        // Test with string contains upper case letters
        assertEquals("ABCDEF", SoundexUtils.clean("aBcDeF"));

        // Test with string contains lower case letters, digits and special characters
        assertEquals("ABCDEF", SoundexUtils.clean("a1b$c2d%e3f"));
    }

    @Test
    public void testDifference() throws EncoderException {
        // Test with null strings
        assertEquals(0, SoundexUtils.difference(null, null, null));

        // Test with empty strings
        assertEquals(0, SoundexUtils.difference(null, "", ""));

        // Test with strings encoded to be the same
        assertEquals(4, SoundexUtils.difference(null, "ABCD", "ABCD"));

        // Test with strings encoded to be different
        assertEquals(0, SoundexUtils.difference(null, "ABCD", "1234"));
    }

    @Test
    public void testDifferenceEncoded() {
        // Test with null strings
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));

        // Test with empty strings
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));

        // Test with strings encoded to be the same
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));

        // Test with strings encoded to be different
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "1234"));
    }
}