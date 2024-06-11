import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABCD", SoundexUtils.clean("AbcD"));
        assertEquals("EFGH", SoundexUtils.clean("Efgh"));
        assertEquals("IJKL", SoundexUtils.clean("Ijkl"));
        assertEquals("MNOP", SoundexUtils.clean("Mnop"));
        assertEquals("QRST", SoundexUtils.clean("Qrst"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, SoundexUtils.difference(soundex, "Test", "Best"));
        assertEquals(2, SoundexUtils.difference(soundex, "Test", "Teat"));
        assertEquals(3, SoundexUtils.difference(soundex, "Test", "Tessa"));
        assertEquals(4, SoundexUtils.difference(soundex, "Test", "Tess"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("A123", "B456"));
        assertEquals(2, SoundexUtils.differenceEncoded("A123", "A124"));
        assertEquals(3, SoundexUtils.differenceEncoded("A123", "A129"));
        assertEquals(4, SoundexUtils.differenceEncoded("A123", "A125"));
    }
}