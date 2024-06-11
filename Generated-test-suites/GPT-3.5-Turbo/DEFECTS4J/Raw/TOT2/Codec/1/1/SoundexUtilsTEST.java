import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("1A!B@c#"));
        assertEquals(null, SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(0, SoundexUtils.difference(null, "hello", "world"));
        assertEquals(4, SoundexUtils.difference(null, "hello", "hello"));
        assertEquals(1, SoundexUtils.difference(null, "hello", "hallo"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABC"));
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
        assertEquals(2, SoundexUtils.differenceEncoded("1234", "12"));
    }
}