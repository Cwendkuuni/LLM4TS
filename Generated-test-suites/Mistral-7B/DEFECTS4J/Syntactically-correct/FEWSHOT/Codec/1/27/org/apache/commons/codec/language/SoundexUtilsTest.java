package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

import java.util.Locale;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("ABC", SoundexUtils.clean("AbC123"));
        assertEquals("ABC", SoundexUtils.clean("AbCdEf123"));
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        assertEquals("ABC", SoundexUtils.clean("aBc"));
        assertEquals("ABC", SoundexUtils.clean("AbC"));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SoundexUtils.clean("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }

    @Test
    public void testDifference() throws EncoderException {
        SoundexEncoder encoder = new SoundexEncoder();
        // identical strings
        assertEquals(4, SoundexUtils.difference(encoder, "Alice", "Alice"));
        assertEquals(4, SoundexUtils.difference(encoder, "Alice", "Alice Smith"));
        // one letter different
        assertEquals(3, SoundexUtils.difference(encoder, "Alice", "Bob"));
        assertEquals(3, SoundexUtils.difference(encoder, "Alice", "Alicia"));
        // two letters different
        assertEquals(2, SoundexUtils.difference(encoder, "Alice", "Bob Smith"));
        assertEquals(2, SoundexUtils.difference(encoder, "Alice", "Alice Brown"));
        // three letters different
        assertEquals(1, SoundexUtils.difference(encoder, "Alice", "Betty Smith"));
        assertEquals(1, SoundexUtils.difference(encoder, "Alice", "Alice Jones"));
        // no letters the same
        assertEquals(0, SoundexUtils.difference(encoder, "Alice", "123"));
        assertEquals(0, SoundexUtils.difference(encoder, "Alice", ""));
    }

    @Test
    public void testDifferenceEncoded() {
        SoundexEncoder encoder = new SoundexEncoder();
        String encodedAlice = encoder.encode("Alice");
        String encodedBob = encoder.encode("Bob");
        String encodedAliceSmith = encoder.encode("Alice Smith");
        String encodedBetty = encoder.encode("Betty");
        String encodedAliceBrown = encoder.encode("Alice Brown");
        String encodedAlice123 = encoder.encode("Alice 123");
        String encodedEmpty = encoder.encode("");

        assertEquals(4, SoundexUtils.differenceEncoded(encodedAlice, encodedAlice));
        assertEquals(4, SoundexUtils.differenceEncoded(encodedAlice, encodedAliceSmith));
        // identical encoded strings with different lengths
        assertEquals(4, SoundexUtils.differenceEncoded(encodedAlice.substring(0, 4), encodedAliceSmith));

        // one letter different
        assertEquals(3, SoundexUtils.differenceEncoded(encodedAlice, encodedBob));
        assertEquals(3, SoundexUtils.differenceEncoded(encodedAlice, encodedAlice.substring(0, 3) + "a"));

        // two letters different
        assertEquals(2, SoundexUtils.differenceEncoded(encodedAlice, encodedAliceSmith.substring(0, 3) + "s"));
        assertEquals(2, SoundexUtils.differenceEncoded(encodedAlice, encodedAliceBrown.substring(0, 3) + "r"));

        // no letters the same
        assertEquals(0, SoundexUtils.differenceEncoded(encodedAlice, encodedAlice123));
        assertEquals(0, SoundexUtils.differenceEncoded(encodedAlice, encodedEmpty));
    }

    @Test
    public void testSoundexEncoder() throws EncoderException {
        SoundexEncoder encoder = new SoundexEncoder();
        String encodedAlice = encoder.encode("Alice");
        assertSame("K220", encodedAlice);
        assertEquals("", encoder.encode(""));
        assertEquals("A123", encoder.encode("A123"));
        assertEquals("B411", encoder.encode("Bob"));
        assertEquals("A123", encoder.encode("Alice Smith"));
        assertEquals("A123", encoder.encode("Alice Brown"));
        assertEquals("A123S311", encoder.encode("Alice Smith"));
        assertEquals("A123R311", encoder.encode("Alice Brown"));
        assertEquals("B411S311", encoder.encode("Bob Smith"));
        assertEquals("B411R311", encoder.encode("Bob Brown"));
    }

    @Test(expected = EncoderException.class)
    public void testSoundexEncoderException() throws EncoderException {
        SoundexEncoder encoder = new SoundexEncoder();
        encoder.encode("!");
    }
}