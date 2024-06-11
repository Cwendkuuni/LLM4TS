import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("A!B#C$"));
        assertEquals("DEF", SoundexUtils.clean("D2E!F4"));
        assertEquals("GHI", SoundexUtils.clean("G577H!9I"));
        assertNull(SoundexUtils.clean(null));
        assertNull(SoundexUtils.clean(""));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(4, SoundexUtils.difference(encoder, "foo", "foo"));
        assertEquals(3, SoundexUtils.difference(encoder, "foo", "fo"));
        assertEquals(3, SoundexUtils.difference(encoder, "foo", "bar"));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));
        assertEquals(1, SoundexUtils.difference(encoder, "foo", "bar"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
        assertEquals(3, SoundexUtils.differenceEncoded("1234", "12"));
        assertEquals(3, SoundexUtils.differenceEncoded("1234", "5678"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(1, SoundexUtils.differenceEncoded("1234", "5678"));
    }
}