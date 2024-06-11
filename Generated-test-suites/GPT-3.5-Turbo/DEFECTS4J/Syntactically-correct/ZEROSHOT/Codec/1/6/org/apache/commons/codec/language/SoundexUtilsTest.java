import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        assertEquals("HELLO", SoundexUtils.clean("heLlo"));
        assertEquals("WORLD", SoundexUtils.clean("W0r1d"));
        assertEquals("EXAMPLE", SoundexUtils.clean("ex@mple"));
        assertEquals("JAVA", SoundexUtils.clean("java"));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, SoundexUtils.difference(soundex, "Hello", "World"));
        
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        assertEquals(8, SoundexUtils.difference(refinedSoundex, "Hello", "World"));
    }
    
    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("1234", "5678"));
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
        assertEquals(3, SoundexUtils.differenceEncoded("abcde", "abdef"));
        assertEquals(5, SoundexUtils.differenceEncoded("soundex", "soundex"));
    }
    
}