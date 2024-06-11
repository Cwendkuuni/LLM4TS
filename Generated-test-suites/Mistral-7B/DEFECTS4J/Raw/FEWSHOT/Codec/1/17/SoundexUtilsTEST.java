package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("foo", SoundexUtils.clean("foo"));
        assertEquals("Foo", SoundexUtils.clean("FOO"));
        assertEquals("Bar", SoundexUtils.clean("BaR"));
        assertEquals("BAR", SoundexUtils.clean("BaR"));
        assertEquals("The quick brown fox jumps over the lazy dog", SoundexUtils.clean("The quick brown fox jumps over the lazy dog"));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsExceptionOnNull() throws EncoderException {
        SoundexUtils.difference(null, "", "");
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsExceptionOnNullInput() throws EncoderException {
        SoundexUtils.difference(new SoundexEncoder(), null, "");
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsExceptionOnEmptyInput() throws EncoderException {
        SoundexUtils.difference(new SoundexEncoder(), "", "");
    }

    @Test
    public void testDifferenceSoundex() {
        assertEquals(2, SoundexUtils.difference(new SoundexEncoder(), "cat", "chat"));
        assertEquals(4, SoundexUtils.difference(new SoundexEncoder(), "apple", "apples"));
        assertEquals(0, SoundexUtils.difference(new SoundexEncoder(), "cat", "dog"));
        assertEquals(0, SoundexUtils.difference(new SoundexEncoder(), "cat", "CAT"));
        assertEquals(3, SoundexUtils.difference(new SoundexEncoder(), "cat", "Cats"));
        assertEquals(4, SoundexUtils.difference(new SoundexEncoder(), "cat", "KAT"));
        assertEquals(1, SoundexUtils.difference(new SoundexEncoder(), "cat", "cAt"));
        assertEquals(1, SoundexUtils.difference(new SoundexEncoder(), "cat", "Cat"));
        assertEquals(1, SoundexUtils.difference(new SoundexEncoder(), "cat", "Cat."));
        assertEquals(1, SoundexUtils.difference(new SoundexEncoder(), "cat", "cat"));
        assertEquals(1, SoundexUtils.difference(new SoundexEncoder(), "cat", "CAT."));
        assertEquals(1, SoundexUtils.difference(new SoundexEncoder(), "cat", "Cat."));
    }

    @Test
    public void testDifferenceRefinedSoundex() {
        SoundexEncoder encoder = new SoundexEncoder();
        assertEquals(5, SoundexUtils.difference(encoder, "cat", "Catherine"));
        assertEquals(5, SoundexUtils.difference(encoder, "cat", "Catherine's"));
        assertEquals(5, SoundexUtils.difference(encoder, "cat", "Katherine"));
        assertEquals(5, SoundxUtils.difference(encoder, "cat", "Katherine's"));
        assertEquals(5, SoundxUtils.difference(encoder, "cat", "Catharine"));
        assertEquals(5, SoundxUtils.difference(encoder, "cat", "Catharine's"));
        assertEquals(5, SoundxUtils.difference(encoder, "cat", "Catherina"));
        assertEquals(5, SoundxUtils.difference(encoder, "cat", "Catherina's"));
        assertEquals(4, SoundxUtils.difference(encoder, "cat", "Kitten"));
        assertEquals(4, SoundxUtils.difference(encoder, "cat", "Kittens"));
        assertEquals(4, SoundxUtils.difference(encoder, "cat", "Kat"));
        assertEquals(4, SoundxUtils.difference(encoder, "cat", "Kats"));
        assertEquals(4, SoundxUtils.difference(encoder, "cat", "Catt"));
        assertEquals(4, SoundxUtils.difference(encoder, "cat", "Catts"));
    }

    @Test
    public void testDifferenceEncoded() {
        String soundex1 = new SoundexEncoder().encode("cat");
        String soundex2 = new SoundexEncoder().encode("chat");
        assertEquals(2, SoundexUtils.differenceEncoded(soundex1, soundex2));

        String soundex1_refined = new SoundexEncoder().encode("cat");
        String soundex2_refined = new SoundexEncoder().encode("Catherine");
        assertEquals(5, SoundexUtils.differenceEncoded(soundex1_refined, soundex2_refined));
    }

    static class SoundexEncoder implements StringEncoder {
        @Override
        public String encode(String string) throws EncoderException {
            return SoundexUtils.soundex(string);
        }
    }

}