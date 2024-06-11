import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "123ABC!@#$";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "halo";
        int expected = 2;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "1234";
        String es2 = "12";
        int expected = 2;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

}