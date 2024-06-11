import static org.junit.Assert.*;

import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("a1b2c3"));
        assertEquals("HELLO", SoundexUtils.clean("HELLO"));
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testDifference() {
        try {
            assertEquals(4, SoundexUtils.difference(new Soundex(), "hello", "HELLO"));
            assertEquals(1, SoundexUtils.difference(new RefinedSoundex(), "hello", "world"));
        } catch (EncoderException e) {
            // Handle the exception
        }
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("AAAA", "AAAA"));
        assertEquals(0, SoundexUtils.differenceEncoded("1234", "5678"));
        assertEquals(3, SoundexUtils.differenceEncoded("HELLO", "HELLI"));
    }

}