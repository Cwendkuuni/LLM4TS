package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals(0, soundex.difference(null, null));
        assertEquals(0, soundex.difference("", ""));
        assertEquals(0, soundex.difference("foo", "foo"));

        assertEquals(1, soundex.difference("foo", "bar"));
        assertEquals(1, soundex.difference("bar", "foo"));
        assertEquals(1, soundex.difference("foo", "foo bar"));
        assertEquals(1, soundex.difference("foo bar", "foo"));

        assertEquals(2, soundex.difference("FOO", "BAR"));
        assertEquals(2, soundex.difference("BAR", "FOO"));

        assertEquals(3, soundex.difference("FOO", "FOO BAR"));
        assertEquals(3, soundex.difference("FOO BAR", "FOO"));

        assertEquals(4, soundex.difference("FOO", "FOO FOO"));
        assertEquals(4, soundex.difference("FOO FOO", "FOO"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;

        assertEquals("A000", soundex.encode("AAA"));
        assertEquals("A220", soundex.encode("APPLE"));
        assertEquals("B331", soundex.encode("BOB"));
        assertEquals("B331", soundex.encode("BOBB"));
        assertEquals("B622", soundex.encode("BILLY"));
        assertEquals("C200", soundex.encode("CAT"));
        assertEquals("D222", soundex.encode("DOG"));
        assertEquals("D222", soundex.encode("DOGS"));
        assertEquals("F111", soundex.encode("FOO"));
        assertEquals("F111", soundex.encode("FOO BAR"));
        assertEquals("F111", soundex.encode("FOO FOO"));
        assertEquals("F111", soundex.encode("FOO FOO FOO"));
        assertEquals("F421", soundex.encode("FORK"));
        assertEquals("G222", soundex.encode("GOOD"));
        assertEquals("G222", soundex.encode("GOODS"));
        assertEquals("H111", soundex.encode("HELLO"));
        assertEquals("H111", soundex.encode("HELLO WORLD"));
        assertEquals("H111", soundex.encode("HELLO WORLD HELLO"));
        assertEquals("H111", soundex.encode("HELLO WORLD HELLO WORLD"));
        assertEquals("H211", soundex.encode("HELLO WORLD HELLO WORLD HELLO"));
        assertEquals("H211", soundex.encode("HELLO WORLD HELLO WORLD HELLO WORLD"));
        assertEquals("H211", soundex.encode("HELLO WORLD HELLO WORLD HELLO WORLD HELLO WORLD"));

        assertEquals("R200", soundex.encode("RABBIT"));
        assertEquals("R200", soundex.encode("RABBIT HOP"));
        assertEquals("R200", soundex.encode("RABBIT HOP HOP"));
        assertEquals("R200", soundex.encode("RABBIT HOP HOP HOP"));
        assertEquals("R200", soundex.encode("RABBIT HOP HOP HOP HOP"));
        assertEquals("R200", soundex.encode("RABBIT HOP HOP HOP HOP HOP"));
        assertEquals("R200", soundex.encode("RABBIT HOP HOP HOP HOP HOP HOP"));

        assertEquals("S111", soundex.encode("SAM SAM SAM SAM"));
        assertEquals("S111", soundex.encode("SAM SAM SAM SAM SAM SAM SAM"));
        assertEquals("S111", soundex.encode("SAM SAM SAM SAM SAM SAM SAM SAM SAM"));

        assertEquals("T210", soundex.encode("THE THE"));
        assertEquals("T210", soundex.encode("THE THE THE"));
        assertEquals("T210", soundex.encode("THE THE THE THE"));
        assertEquals("T210", soundxencode.encode("THE THE THE THE THE"));

        assertEquals("W111", soundex.encode("WATER WATER"));
        assertEquals("W111", soundex.encode("WATER WATER WATER"));
        assertEquals("W111", soundex.encode("WATER WATER WATER WATER"));
        assertEquals("W111", soundex.encode("WATER WATER WATER WATER WATER"));
        assertEquals("W111", soundex.encode("WATER WATER WATER WATER WATER WATER"));
        assertEquals("W111", soundex.encode("WATER WATER WATER WATER WATER WATER WATER"));
        assertEquals("W111", soundex.encode("WATER WATER WATER WATER WATER WATER WATER WATER"));

        assertEquals("X200", soundex.encode("XMAS XMAS"));
        assertEquals("X200", soundex.encode("XMAS XMAS XMAS"));
        assertEquals("X200", soundex.encode("XMAS XMAS XMAS XMAS"));
        assertEquals("X200", soundex.encode("XMAS XMAS XMAS XMAS XMAS"));
        assertEquals("X200", soundex.encode("XMAS XMAS XMAS XMAS XMAS XMAS"));
        assertEquals("X200", soundex.encode("XMAS XMAS XMAS XMAS XMAS XMAS XMAS"));
        assertEquals("X200", soundex.encode("XMAS XMAS XMAS XMAS XMAS XMAS XMAS XMAS"));

        assertEquals("Y111", soundex.encode("YES YES YES"));
        assertEquals("Y111", soundex.encode("YES YES YES YES"));
        assertEquals("Y111", soundex.encode("YES YES YES YES YES"));
        assertEquals("Y111", soundex.encode("YES YES YES YES YES YES"));
        assertEquals("Y111", soundex.encode("YES YES YES YES YES YES YES"));
        assertEquals("Y111", soundex.encode("YES YES YES YES YES YES YES YES"));
        assertEquals("Y111", soundex.encode("YES YES YES YES YES YES YES YES YES"));

        assertEquals("Z111", soundex.encode("ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundex.encode("ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundex.encode("ZOO ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundex.encode("ZOO ZOO ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundex.encode("ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundex.encode("ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundex.encode("ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO"));

        // Test with different mappings
        Soundex soundexCustom = new Soundex(new char[]{"01230120022455012623010202"});
        assertEquals("A000", soundexCustom.encode("AAA"));
        assertEquals("A220", soundexCustom.encode("APPLE"));
        assertEquals("B331", soundexCustom.encode("BOB"));
        assertEquals("B331", soundexCustom.encode("BOBB"));
        assertEquals("B622", soundexCustom.encode("BILLY"));
        assertEquals("C200", soundexCustom.encode("CAT"));
        assertEquals("D222", soundexCustom.encode("DOG"));
        assertEquals("D222", soundexCustom.encode("DOGS"));
        assertEquals("F111", soundexCustom.encode("FOO"));
        assertEquals("F111", soundexCustom.encode("FOO BAR"));
        assertEquals("F111", soundexCustom.encode("FOO FOO"));
        assertEquals("F111", soundexCustom.encode("FOO FOO FOO"));
        assertEquals("F421", soundexCustom.encode("FORK"));
        assertEquals("G222", soundexCustom.encode("GOOD"));
        assertEquals("G222", soundexCustom.encode("GOODS"));
        assertEquals("H111", soundexCustom.encode("HELLO"));
        assertEquals("H111", soundexCustom.encode("HELLO WORLD"));
        assertEquals("H111", soundexCustom.encode("HELLO WORLD HELLO"));
        assertEquals("H111", soundexCustom.encode("HELLO WORLD HELLO WORLD"));
        assertEquals("H211", soundexCustom.encode("HELLO WORLD HELLO WORLD HELLO"));
        assertEquals("H211", soundexCustom.encode("HELLO WORLD HELLO WORLD HELLO WORLD"));
        assertEquals("H211", soundexCustom.encode("HELLO WORLD HELLO WORLD HELLO WORLD HELLO WORLD"));

        assertEquals("R200", soundexCustom.encode("RABBIT"));
        assertEquals("R200", soundexCustom.encode("RABBIT HOP"));
        assertEquals("R200", soundexCustom.encode("RABBIT HOP HOP"));
        assertEquals("R200", soundexCustom.encode("RABBIT HOP HOP HOP"));
        assertEquals("R200", soundexCustom.encode("RABBIT HOP HOP HOP HOP"));
        assertEquals("R200", soundexCustom.encode("RABBIT HOP HOP HOP HOP HOP"));
        assertEquals("R200", soundexCustom.encode("RABBIT HOP HOP HOP HOP HOP HOP"));

        assertEquals("S111", soundexCustom.encode("SAM SAM SAM SAM"));
        assertEquals("S111", soundexCustom.encode("SAM SAM SAM SAM SAM SAM SAM"));
        assertEquals("S111", soundexCustom.encode("SAM SAM SAM SAM SAM SAM SAM SAM SAM"));

        assertEquals("T210", soundexCustom.encode("THE THE"));
        assertEquals("T210", soundexCustom.encode("THE THE THE"));
        assertEquals("T210", soundexCustom.encode("THE THE THE THE"));
        assertEquals("T210", soundexCustom.encode("THE THE THE THE THE"));

        assertEquals("W111", soundexCustom.encode("WATER WATER"));
        assertEquals("W111", soundexCustom.encode("WATER WATER WATER"));
        assertEquals("W111", soundexCustom.encode("WATER WATER WATER WATER"));
        assertEquals("W111", soundexCustom.encode("WATER WATER WATER WATER WATER"));
        assertEquals("W111", soundexCustom.encode("WATER WATER WATER WATER WATER WATER"));
        assertEquals("W111", soundexCustom.encode("WATER WATER WATER WATER WATER WATER WATER"));
        assertEquals("W111", soundexCustom.encode("WATER WATER WATER WATER WATER WATER WATER WATER"));

        assertEquals("X200", soundexCustom.encode("XMAS XMAS"));
        assertEquals("X200", soundexCustom.encode("XMAS XMAS XMAS"));
        assertEquals("X200", soundexCustom.encode("XMAS XMAS XMAS XMAS"));
        assertEquals("X200", soundexCustom.encode("XMAS XMAS XMAS XMAS XMAS"));
        assertEquals("X200", soundexCustom.encode("XMAS XMAS XMAS XMAS XMAS XMAS"));
        assertEquals("X200", soundexCustom.encode("XMAS XMAS XMAS XMAS XMAS XMAS XMAS"));
        assertEquals("X200", soundexCustom.encode("XMAS XMAS XMAS XMAS XMAS XMAS XMAS XMAS"));

        assertEquals("Y111", soundexCustom.encode("YES YES YES"));
        assertEquals("Y111", soundexCustom.encode("YES YES YES YES"));
        assertEquals("Y111", soundexCustom.encode("YES YES YES YES YES"));
        assertEquals("Y111", soundexCustom.encode("YES YES YES YES YES YES"));
        assertEquals("Y111", soundexCustom.encode("YES YES YES YES YES YES YES"));
        assertEquals("Y111", soundexCustom.encode("YES YES YES YES YES YES YES YES"));
        assertEquals("Y111", soundexCustom.encode("YES YES YES YES YES YES YES YES YES"));

        assertEquals("Z111", soundexCustom.encode("ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundexCustom.encode("ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundexCustom.encode("ZOO ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundexCustom.encode("ZOO ZOO ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundexCustom.encode("ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundexCustom.encode("ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO"));
        assertEquals("Z111", soundexCustom.encode("ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO ZOO"));

        // Test empty string and null
        assertNull(soundex.encode(null));
        assertEquals("", soundex.encode(""));
    }
}