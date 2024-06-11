import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "abc123!@#";
        String expected = "ABC";
        assertEquals(expected, SoundexUtils.clean(input));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new YourEncoder(); // Replace YourEncoder with actual encoder
        String s1 = "hello";
        String s2 = "hello";
        int expected = 5; // For RefinedSoundex
        assertEquals(expected, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "12345";
        String es2 = "12345";
        int expected = 5;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
    }

}