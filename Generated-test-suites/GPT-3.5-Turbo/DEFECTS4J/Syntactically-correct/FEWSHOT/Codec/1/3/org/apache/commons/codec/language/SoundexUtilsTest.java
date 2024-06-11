import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("abc"));
        assertEquals("DEF", SoundexUtils.clean("def"));
        assertEquals("GHI", SoundexUtils.clean("ghi"));
        assertEquals("JKL", SoundexUtils.clean("jkl"));
        assertEquals("MNO", SoundexUtils.clean("mno"));
        assertEquals("PQR", SoundexUtils.clean("pqr"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new SoundexEncoder();
        assertEquals(0, SoundexUtils.difference(encoder, "abc", "def"));
        assertEquals(4, SoundexUtils.difference(encoder, "abc", "abc"));
        assertEquals(1, SoundexUtils.difference(encoder, "abc", "bcd"));
        assertEquals(2, SoundexUtils.difference(encoder, "abc", "bde"));
        assertEquals(3, SoundexUtils.difference(encoder, "abc", "ade"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", "DEF"));
        assertEquals(4, SoundexUtils.differenceEncoded("ABC", "ABC"));
        assertEquals(1, SoundexUtils.differenceEncoded("ABC", "BCD"));
        assertEquals(2, SoundexUtils.differenceEncoded("ABC", "BDE"));
        assertEquals(3, SoundexUtils.differenceEncoded("ABC", "ADE"));
    }
}