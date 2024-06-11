
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.RefinedSoundex;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        assertEquals("ABCD", SoundexUtils.clean("aBcD"));
        assertEquals("ABCDE", SoundexUtils.clean("aBc1D2e"));
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, SoundexUtils.difference(soundex, "hello", "hello"));
        assertEquals(3, SoundexUtils.difference(soundex, "hello", "hell"));
        assertEquals(2, SoundexUtils.difference(soundex, "hello", "hel"));
        assertEquals(1, SoundexUtils.difference(soundex, "hello", "he"));
        assertEquals(0, SoundexUtils.difference(soundex, "hello", "h"));
        
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        assertEquals(10, SoundexUtils.difference(refinedSoundex, "hello123", "hello456"));
        assertEquals(9, SoundexUtils.difference(refinedSoundex, "hello123", "hello45"));
        assertEquals(8, SoundexUtils.difference(refinedSoundex, "hello123", "hello4"));
        assertEquals(7, SoundexUtils.difference(refinedSoundex, "hello123", "hello"));
        assertEquals(6, SoundexUtils.difference(refinedSoundex, "hello123", "hell"));
    }
    
    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
        assertEquals(3, SoundexUtils.differenceEncoded("1234", "123"));
        assertEquals(2, SoundexUtils.differenceEncoded("1234", "12"));
        assertEquals(1, SoundexUtils.differenceEncoded("1234", "1"));
        assertEquals(0, SoundexUtils.differenceEncoded("1234", ""));
    }
}

