import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("FOO", SoundexUtils.clean("foo"));
        assertEquals("BAR", SoundexUtils.clean(" bar "));
        assertEquals("", SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(0, SoundexUtils.difference(encoder, "foo", "bar"));
        assertEquals(3, SoundexUtils.difference(encoder, "foo", "foo bar"));
        assertEquals(4, SoundexUtils.difference(encoder, "foo", "foo"));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("FOO", "BAR"));
        assertEquals(3, SoundexUtils.differenceEncoded("FOO", "FOO BAR"));
        assertEquals(4, SoundexUtils.differenceEncoded("FOO", "FOO"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }
}