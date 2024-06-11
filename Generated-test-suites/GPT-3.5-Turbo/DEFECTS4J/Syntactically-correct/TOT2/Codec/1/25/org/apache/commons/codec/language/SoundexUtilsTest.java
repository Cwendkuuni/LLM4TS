import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("HELLO", SoundexUtils.clean("He$llo123")); // Typical use case
        assertEquals("WORLD", SoundexUtils.clean("WORLD")); // Edge case with all uppercase letters
        assertEquals("", SoundexUtils.clean(null)); // Edge case with null input
    }

    @Test
    public void testDifference() {
        try {
            assertEquals(4, SoundexUtils.difference(new Soundex(), "hello", "HELLO")); // Typical use case
            assertEquals(5, SoundexUtils.difference(new RefinedSoundex(), "apple", "apples")); // Edge case with refined Soundex
            assertEquals(0, SoundexUtils.difference(new Soundex(), "123", "456")); // Edge case with no similarity
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown in this test case");
        }
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "12A4")); // Typical use case
        assertEquals(0, SoundexUtils.differenceEncoded("", "")); // Edge case with empty strings
        assertEquals(3, SoundexUtils.differenceEncoded("abc", "abcde")); // Edge case with different lengths
    }

}