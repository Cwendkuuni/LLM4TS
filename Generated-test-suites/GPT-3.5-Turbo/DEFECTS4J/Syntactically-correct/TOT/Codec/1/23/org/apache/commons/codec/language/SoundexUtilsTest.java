import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("aB!c123"));
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        assertEquals("", SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, SoundexUtils.difference(new SoundexEncoder(), "abc", "abc"));
        assertEquals(2, SoundexUtils.difference(new SoundexEncoder(), "abc", "adc"));
        assertEquals(0, SoundexUtils.difference(new SoundexEncoder(), "abc", "def"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
        assertEquals(2, SoundexUtils.differenceEncoded("1234", "1245"));
        assertEquals(0, SoundexUtils.differenceEncoded("1234", "5678"));
        assertEquals(0, SoundexUtils.differenceEncoded(null, "1234"));
        assertEquals(0, SoundexUtils.differenceEncoded("1234", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

}