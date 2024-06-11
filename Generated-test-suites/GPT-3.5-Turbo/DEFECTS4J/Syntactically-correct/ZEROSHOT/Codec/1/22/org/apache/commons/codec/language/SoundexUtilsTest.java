import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean_NullInput() {
        String result = SoundexUtils.clean(null);
        assertNull(result);
    }

    @Test
    public void testClean_EmptyInput() {
        String result = SoundexUtils.clean("");
        assertEquals("", result);
    }

    @Test
    public void testClean_AllLetters() {
        String result = SoundexUtils.clean("abcde");
        assertEquals("ABCDE", result);
    }

    @Test
    public void testClean_MixedLettersAndNonLetters() {
        String result = SoundexUtils.clean("a1b2c3d4e5");
        assertEquals("ABCDE", result);
    }

    @Test
    public void testDifference_SameEncoder_SameStrings() throws EncoderException {
        int result = SoundexUtils.difference(new Soundex(), "abcde", "abcde");
        assertEquals(4, result);
    }

    @Test
    public void testDifference_SameEncoder_DifferentStrings() throws EncoderException {
        int result = SoundexUtils.difference(new Soundex(), "abcde", "xyz");
        assertEquals(0, result);
    }

    @Test
    public void testDifference_DifferentEncoder_SameStrings() throws EncoderException {
        int result = SoundexUtils.difference(new RefinedSoundex(), "abcde", "abcde");
        assertEquals(4, result);
    }

    @Test
    public void testDifference_DifferentEncoder_DifferentStrings() throws EncoderException {
        int result = SoundexUtils.difference(new RefinedSoundex(), "abcde", "xyz");
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncoded_SameStrings() {
        int result = SoundexUtils.differenceEncoded("ABCD", "ABCD");
        assertEquals(4, result);
    }

    @Test
    public void testDifferenceEncoded_DifferentStrings() {
        int result = SoundexUtils.differenceEncoded("ABCD", "XYZ");
        assertEquals(0, result);
    }

}