package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABCDE", SoundexUtils.clean("ABCDE"));
        assertEquals("ABCde", SoundexUtils.clean("ABCde"));
        assertEquals("ABCde FGHIJ", SoundexUtils.clean("ABCde FGHIJ"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("A B C D E", SoundexUtils.clean("A B C D E"));
        assertEquals("A B C D E", SoundexUtils.clean(" A B C D E "));
        assertEquals("A B C D E", SoundexUtils.clean("A   B   C   D   E"));
        assertEquals("A B C D E", SoundexUtils.clean("A B C D E   "));
        assertEquals("A B C D E", SoundexUtils.clean("A B C D E\n"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, SoundexUtils.difference(soundex, "cat", "bat"));
        assertEquals(3, SoundexUtils.difference(soundex, "cat", "sit"));
        assertEquals(4, SoundexUtils.difference(soundex, "apple", "appel"));
        assertEquals(4, SoundexUtils.difference(soundex, "apple", "apfel"));
        assertEquals(4, SoundexUtils.difference(soundex, "apple", "appl"));
        assertEquals(4, SoundexUtils.difference(soundex, "alpha", "alpha"));
        assertEquals(0, SoundexUtils.difference(soundex, "alpha", "beta"));
        assertEquals(1, SoundexUtils.difference(soundex, "alpha", "alphabet"));
        assertNull(SoundexUtils.difference(soundex, null, "beta"));
        assertNull(SoundexUtils.difference(soundex, "alpha", null));
    }

    @Test
    public void testDifferenceEncoded() {
        String rawSoundex = SoundexUtils.differenceEncoded("KXUCUW", "KWOTCX");
        assertEquals(3, rawSoundex.length());
        assertEquals("KW", rawSoundex.substring(0, 2));
        assertEquals("CX", rawSoundex.substring(2, 4));

        rawSoundex = SoundexUtils.differenceEncoded("KXUCUW", "KXUCXW");
        assertEquals(3, rawSoundex.length());
        assertEquals("UC", rawSoundex.substring(0, 2));
        assertEquals("CX", rawSoundex.substring(2, 4));

        rawSoundex = SoundexUtils.differenceEncoded("KXUCUW", "KXUCUW");
        assertEquals(4, rawSoundex.length());
        assertEquals("UCWU", rawSoundex);

        rawSoundex = SoundexUtils.differenceEncoded("KWOTCX", "KWOTCX");
        assertEquals(4, rawSoundex.length());
        assertEquals("OTCX", rawSoundex);

        rawSoundex = SoundexUtils.differenceEncoded("KWOTCX", null);
        assertNull(rawSoundex);

        rawSoundex = SoundexUtils.differenceEncoded(null, "KWOTCX");
        assertNull(rawSoundex);
    }

}