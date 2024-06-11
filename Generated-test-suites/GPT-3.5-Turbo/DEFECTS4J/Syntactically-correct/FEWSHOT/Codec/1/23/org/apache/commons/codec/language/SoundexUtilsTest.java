import org.junit.Test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("HELLO", SoundexUtils.clean("Hello"));
        assertEquals("APACHE", SoundexUtils.clean("Apache123"));
        assertEquals("I123", SoundexUtils.clean("i123"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(4, SoundexUtils.difference(encoder, "hello", "hullo"));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));
        assertEquals(0, SoundexUtils.difference(encoder, null, "hello"));
        assertEquals(0, SoundexUtils.difference(encoder, "hello", null));

        encoder = new RefinedSoundex();
        assertEquals(4, SoundexUtils.difference(encoder, "hello", "hullo"));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));
        assertEquals(0, SoundexUtils.difference(encoder, null, "hello"));
        assertEquals(0, SoundexUtils.difference(encoder, "hello", null));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(2, SoundexUtils.differenceEncoded("ABC", "ABD"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", "BCD"));
    }
}