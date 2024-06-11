import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("Abc123"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("XYZ", SoundexUtils.clean("x yz"));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SoundexUtils.clean("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("XYZ", SoundexUtils.clean("123xyz456"));
        assertEquals("XYZ", SoundexUtils.clean("XYZ"));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(0, SoundexUtils.difference(new Soundex(), "abc", "def"));
        assertEquals(0, SoundexUtils.difference(new RefinedSoundex(), "abc", "def"));
        assertEquals(4, SoundexUtils.difference(new Soundex(), "abc", "abc"));
        assertEquals(4, SoundexUtils.difference(new RefinedSoundex(), "abc", "abc"));
        assertEquals(3, SoundexUtils.difference(new Soundex(), "abc", "abd"));
        assertEquals(3, SoundexUtils.difference(new RefinedSoundex(), "abc", "abd"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("abc", "def"));
        assertEquals(4, SoundexUtils.differenceEncoded("abc", "abc"));
        assertEquals(3, SoundexUtils.differenceEncoded("abc", "abd"));
    }

}