import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("a1b2c3"));
        assertEquals("XYZ", SoundexUtils.clean(" x y z "));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(null, SoundexUtils.clean(null));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, SoundexUtils.difference(new Soundex(), "abc", "abc"));
        assertEquals(0, SoundexUtils.difference(new Soundex(), "abc", "def"));
        assertEquals(5, SoundexUtils.difference(new RefinedSoundex(), "abcde", "abcde"));
        assertEquals(0, SoundexUtils.difference(new RefinedSoundex(), "12345", "67890"));
    }
    
    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
        assertEquals(0, SoundexUtils.differenceEncoded("1234", "5678"));
        assertEquals(3, SoundexUtils.differenceEncoded("AAAA", "AAAB"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

}