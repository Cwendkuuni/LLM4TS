import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("HELLO", SoundexUtils.clean("Hello123"));
        assertEquals("WORLD", SoundexUtils.clean("W@orl123d"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(null, SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() {
        try {
            assertEquals(4, SoundexUtils.difference(new Soundex(), "hello", "hello"));
            assertEquals(2, SoundexUtils.difference(new Soundex(), "hello", "world"));
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("H450", "H450"));
        assertEquals(2, SoundexUtils.differenceEncoded("H450", "W400"));
        assertEquals(0, SoundexUtils.differenceEncoded("", "W400"));
    }

}