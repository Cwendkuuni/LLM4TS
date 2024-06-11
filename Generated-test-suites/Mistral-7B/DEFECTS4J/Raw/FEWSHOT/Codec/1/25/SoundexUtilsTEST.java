package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.language.Soundex;

/**
 * Test cases for {@link SoundexUtils} class.
 */
public class SoundexUtilsTest {

    /**
     * Test for {@link SoundexUtils#clean(String)} method.
     */
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("AbC123"));
        assertEquals("ABC", SoundexUtils.clean("aBc"));
        assertEquals("ABC", SoundexUtils.clean("AbC dEf 123"));
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method.
     */
    @Test
    public void testDifferenceEncoded() {
        Soundex soundex = new Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        String es1 = soundex.encode(s1);
        String es2 = soundex.encode(s2);

        assertEquals(1, SoundexUtils.differenceEncoded(es1, es2));
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es1));
        assertEquals(4, SoundexUtils.differenceEncoded(es1, es1.toUpperCase()));
        assertNull(SoundexUtils.differenceEncoded(null, ""));
        assertNull(SoundexUtils.differenceEncoded("", null));
        assertNull(SoundexUtils.differenceEncoded(null, null));

        // Test with Hex encoded strings
        String hex1 = new String(Hex.encodeHex(soundex.encode(s1).getBytes()));
        String hex2 = new String(Hex.encodeHex(soundex.encode(s2).getBytes()));
        assertEquals(1, SoundexUtils.differenceEncoded(hex1, hex2));
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)} method.
     */
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        String s3 = "Apple";

        assertEquals(1, SoundexUtils.difference(soundex, s1, s2));
        assertEquals(3, SoundexUtils.difference(soundex, s1, s1));
        assertEquals(4, SoundexUtils.difference(soundex, s1, s3));
        assertNull(SoundexUtils.difference(soundex, null, ""));
        assertNull(SoundexUtils.difference(soundex, "", null));
        assertNull(SoundexUtils.difference(soundex, null, null));
    }

}