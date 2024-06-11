import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean_nullInput() {
        String result = SoundexUtils.clean(null);
        assertNull(result);
    }

    @Test
    public void testClean_emptyInput() {
        String result = SoundexUtils.clean("");
        assertEquals("", result);
    }

    @Test
    public void testClean_noLetters() {
        String result = SoundexUtils.clean("123");
        assertEquals("", result);
    }

    @Test
    public void testClean_uppercaseLetters() {
        String result = SoundexUtils.clean("ABC");
        assertEquals("ABC", result);
    }

    @Test
    public void testClean_lowercaseLetters() {
        String result = SoundexUtils.clean("abc");
        assertEquals("ABC", result);
    }

    @Test
    public void testClean_mixedCaseLetters() {
        String result = SoundexUtils.clean("aBc");
        assertEquals("ABC", result);
    }

    @Test
    public void testClean_withSpecialCharacters() {
        String result = SoundexUtils.clean("abc@123");
        assertEquals("ABC", result);
    }

    @Test
    public void testDifference_sameEncoder() throws EncoderException {
        int result = SoundexUtils.difference(new Soundex(), "ABC", "DEF");
        assertEquals(0, result);
    }

    @Test
    public void testDifference_differentEncoder() throws EncoderException {
        int result = SoundexUtils.difference(new RefinedSoundex(), "ABC", "DEF");
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncoded_nullInput() {
        int result = SoundexUtils.differenceEncoded(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncoded_emptyInput() {
        int result = SoundexUtils.differenceEncoded("", "");
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncoded_sameInput() {
        int result = SoundexUtils.differenceEncoded("ABC", "ABC");
        assertEquals(3, result);
    }

    @Test
    public void testDifferenceEncoded_differentInput() {
        int result = SoundexUtils.differenceEncoded("ABC", "DEF");
        assertEquals(0, result);
    }

}