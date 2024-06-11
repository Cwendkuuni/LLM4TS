
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.language.RefinedSoundex;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void clean_NullString_ReturnsNull() {
        String result = SoundexUtils.clean(null);
        assertNull(result);
    }

    @Test
    public void clean_EmptyString_ReturnsEmptyString() {
        String result = SoundexUtils.clean("");
        assertEquals("", result);
    }

    @Test
    public void clean_OnlyUpperCaseLetters_ReturnsUpperCaseString() {
        String result = SoundexUtils.clean("HELLO");
        assertEquals("HELLO", result);
    }

    @Test
    public void clean_MixedCaseLetters_ReturnsUpperCaseString() {
        String result = SoundexUtils.clean("HeLlo");
        assertEquals("HELLO", result);
    }

    @Test
    public void clean_SpecialCharactersAndNumbers_ReturnsUpperCaseString() {
        String result = SoundexUtils.clean("Hello123!@#$%");
        assertEquals("HELLO", result);
    }

    @Test
    public void difference_SameEncoderAndSimilarStrings_ReturnsPositiveNumber() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = SoundexUtils.difference(soundex, "hello", "hella");
        assertTrue(result > 0);
    }

    @Test
    public void difference_SameEncoderAndDifferentStrings_ReturnsZero() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = SoundexUtils.difference(soundex, "hello", "world");
        assertEquals(0, result);
    }

    @Test
    public void difference_DifferentEncoderAndSimilarStrings_ReturnsPositiveNumber() throws EncoderException {
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        int result = SoundexUtils.difference(refinedSoundex, "hello", "hella");
        assertTrue(result > 0);
    }

    @Test
    public void difference_DifferentEncoderAndDifferentStrings_ReturnsZero() throws EncoderException {
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        int result = SoundexUtils.difference(refinedSoundex, "hello", "world");
        assertEquals(0, result);
    }

    @Test
    public void differenceEncoded_SimilarStrings_ReturnsPositiveNumber() {
        int result = SoundexUtils.differenceEncoded("H200", "H201");
        assertTrue(result > 0);
    }

    @Test
    public void differenceEncoded_DifferentStrings_ReturnsZero() {
        int result = SoundexUtils.differenceEncoded("H200", "W500");
        assertEquals(0, result);
    }

}