package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        // Testing null input
        assertEquals(null, SoundexUtils.clean(null));

        // Testing empty string
        assertEquals("", SoundexUtils.clean(""));

        // Testing input with no letters
        assertEquals("", SoundexUtils.clean("1234"));

        // Testing input with only lowercase letters
        assertEquals("ABC", SoundexUtils.clean("abc"));

        // Testing input with mixed case letters
        assertEquals("ABC", SoundexUtils.clean("aBc123"));

        // Testing input with letters and non-letters
        assertEquals("ABC", SoundexUtils.clean("aBc123@#"));
    }

    /* TODO: Uncomment this test after implementing the encode() method in the encoder
    @Test
    public void testDifference() throws EncoderException {
        // Testing null input
        assertEquals(0, SoundexUtils.difference(null, null));

        // Testing empty strings
        assertEquals(0, SoundexUtils.difference(new SoundexEncoder(), "", ""));
        
        // Testing input with no letters
        assertEquals(0, SoundexUtils.difference(new SoundexEncoder(), "123", "456"));

        // Testing input with letters and numbers
        assertEquals(2, SoundexUtils.difference(new SoundexEncoder(), "abc123", "456abc"));

        // Testing input with lowercase and uppercase letters
        assertEquals(4, SoundexUtils.difference(new SoundexEncoder(), "abc", "ABC"));

        // Testing input with encoded strings that have the same characters in the same positions
        assertEquals(4, SoundexUtils.difference(new SoundexEncoder(), "abc", "abc"));

        // Testing input with encoded strings that have different characters in the same positions
        assertEquals(0, SoundexUtils.difference(new SoundexEncoder(), "abc", "def"));
    }
    */

    @Test
    public void testDifferenceEncoded() {
        // Testing null input
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));

        // Testing empty strings
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));

        // Testing input with encoded strings that have the same characters in the same positions
        assertEquals(4, SoundexUtils.differenceEncoded("A123", "A123"));

        // Testing input with encoded strings that have different characters in the same positions
        assertEquals(0, SoundexUtils.differenceEncoded("A123", "B234"));

        // Testing input with encoded strings that have different lengths
        assertEquals(2, SoundexUtils.differenceEncoded("A123", "AB12"));

        // Testing input with encoded strings where one is a prefix of the other
        assertEquals(3, SoundexUtils.differenceEncoded("A123", "A1234"));
    }
}