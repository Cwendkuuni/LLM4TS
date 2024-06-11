import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

public class SoundexUtilsTest {

    Soundex soundex = new Soundex();

    @Test
    public void testClean() {
        assertEquals("S", SoundexUtils.clean("s"));
        assertEquals("ABC", SoundexUtils.clean("abc"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(null, SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, SoundexUtils.difference(soundex, "apple", "apply"));
        assertEquals(0, SoundexUtils.difference(soundex, "", ""));
        assertEquals(0, SoundexUtils.difference(soundex, null, null));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("A123", "A123"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

}