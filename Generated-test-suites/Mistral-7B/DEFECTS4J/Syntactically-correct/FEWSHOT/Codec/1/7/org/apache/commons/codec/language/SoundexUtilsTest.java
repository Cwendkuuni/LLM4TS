package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.Soundex.SoundexEncoder;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("AbCdEfGhIjKlMnOpQrStUvWxYz"));
        assertEquals("ABC", SoundexUtils.clean("aBcD eFgHiJkLmNoPqRsTuVwXyZ"));
        assertEquals("ABC", SoundexUtils.clean("AbCdEfGhIjKlMnOpQrStUvWxYz "));
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("ABC", SoundexUtils.clean("AbCdEfGhIjKlMnOpQrStUvWxYz ").toUpperCase());
    }

    @Test
    public void testDifference() throws EncoderException {
        SoundexEncoder soundexEncoder = new SoundexEncoder();
        int difference = SoundexUtils.difference(soundexEncoder, "Apple", "Orange");
        assertEquals(2, difference);

        difference = SoundexUtils.difference(soundexEncoder, "Apple", "Apple");
        assertEquals(4, difference);

        difference = SoundexUtils.difference(soundexEncoder, "Apple", "Apricot");
        assertEquals(3, difference);

        difference = SoundexUtils.difference(soundexEncoder, "Apple", "Pineapple");
        assertEquals(3, difference);

        difference = SoundexUtils.difference(soundexEncoder, null, "Apple");
        assertEquals(0, difference);

        difference = SoundexUtils.difference(soundexEncoder, "Apple", null);
        assertEquals(0, difference);
    }

    @Test
    public void testDifferenceEncoded() {
        String appleSoundex = SoundexUtils.differenceEncoded(SoundexEncoder.INSTANCE.encode("Apple"), SoundexEncoder.INSTANCE.encode("Apple"));
        assertEquals("1223", appleSoundex);

        String appleOrangeSoundex = SoundexUtils.differenceEncoded(SoundexEncoder.INSTANCE.encode("Apple"), SoundexEncoder.INSTANCE.encode("Orange"));
        assertEquals("2123", appleOrangeSoundex);

        String appleApricotSoundex = SoundexUtils.differenceEncoded(SoundexEncoder.INSTANCE.encode("Apple"), SoundexEncoder.INSTANCE.encode("Apricot"));
        assertEquals("212323", appleApricotSoundex);

        String applePineappleSoundex = SoundexUtils.differenceEncoded(SoundexEncoder.INSTANCE.encode("Apple"), SoundexEncoder.INSTANCE.encode("Pineapple"));
        assertEquals("232323", applePineappleSoundex);

        String appleNullSoundex = SoundexUtils.differenceEncoded(SoundexEncoder.INSTANCE.encode("Apple"), null);
        assertEquals("", appleNullSoundex);

        String nullAppleSoundex = SoundexUtils.differenceEncoded(null, SoundexEncoder.INSTANCE.encode("Apple"));
        assertEquals("", nullAppleSoundex);
    }

    @Test
    public void testDifferenceEncodedWithRefinedSoundex() {
        String appleSoundex = SoundexEncoder.INSTANCE.encode("Apple");
        String orangeSoundex = SoundexEncoder.INSTANCE.encode("Orange");
        String appleApricotSoundex = SoundexEncoder.INSTANCE.encode("Apricot");
        String applePineappleSoundex = SoundexEncoder.INSTANCE.encode("Pineapple");

        String refinedAppleSoundex = new RefineSoundexEncoder().encode("Apple");
        String refinedOrangeSoundex = new RefineSoundexEncoder().encode("Orange");
        String refinedApricotSoundex = new RefineSoundexEncoder().encode("Apricot");
        String refinedPineappleSoundex = new RefineSoundexEncoder().encode("Pineapple");

        int difference = SoundexUtils.differenceEncoded(appleSoundex, orangeSoundex);
        assertEquals(2, difference);

        difference = SoundexUtils.differenceEncoded(appleSoundex, apple);
        assertEquals(4, difference);

        difference = SoundexUtils.differenceEncoded(appleSoundex, appleApricotSoundex);
        assertEquals(3, difference);

        difference = SoundexUtils.differenceEncoded(appleSoundex, applePineappleSoundex);
        assertEquals(3, difference);

        difference = SoundexUtils.differenceEncoded(refinedAppleSoundex, refinedOrangeSoundex);
        assertEquals(2, difference);

        difference = SoundexUtils.differenceEncoded(refinedAppleSoundex, refinedApricotSoundex);
        assertEquals(3, difference);

        difference = SoundexUtils.differenceEncoded(refinedAppleSoundex, refinedPineappleSoundex);
        assertEquals(3, difference);

        difference = SoundexUtils.differenceEncoded(refinedAppleSoundex, null);
        assertEquals(0, difference);

        difference = SoundexUtils.differenceEncoded(null, refinedAppleSoundex);
        assertEquals(0, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithInvalidInput() throws EncoderException {
        SoundexEncoder soundexEncoder = new SoundexEncoder();
        String invalidInput = "!@#$%^&*()_+={}[];:\"|\\<>,.?/~`";
        SoundexUtils.difference(soundexEncoder, "", invalidInput);
    }

    @Test
    public void testCleanWithInvalidInput() {
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("", SoundexUtils.clean("!@#$%^&*()_+={}[];:\"|\\<>,.?/~`"));
    }
}