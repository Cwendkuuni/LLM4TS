import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // typical use case
        assertEquals("TEST", SoundexUtils.clean("Test"));
        // edge case: empty string
        assertEquals("", SoundexUtils.clean(""));
        // edge case: string with non-letter characters
        assertEquals("HELLO", SoundexUtils.clean("Hello123"));
    }

    @Test
    public void testDifference() {
        // typical use case
        assertEquals(4, SoundexUtils.difference(new Soundex(), "Hello", "Halo"));
        // edge case: strings with no similarity
        assertEquals(0, SoundexUtils.difference(new RefinedSoundex(), "Test", "Java"));
        // error scenario: encoder exception
        try {
            SoundexUtils.difference(new Soundex(), "Hello", null);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // pass
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // typical use case
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1235"));
        // edge case: different encoded lengths
        assertEquals(3, SoundexUtils.differenceEncoded("123", "12345"));
        // edge case: same encoded strings
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
    }
}