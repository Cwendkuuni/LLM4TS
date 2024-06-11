import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "abc123DEF!@#";
        String expected = "ABCDEF";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCleanWithEmptyString() {
        String input = "";
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifference() {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "Hello";
        int expected = 4;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceWithNullEncodedStrings() {
        String es1 = null;
        String es2 = "ABCDEF";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithEncoderException() throws EncoderException {
        StringEncoder encoder = new RefinedSoundex();
        String s1 = "hello";
        String s2 = "Refined";
        SoundexUtils.difference(encoder, s1, s2);
    }

}