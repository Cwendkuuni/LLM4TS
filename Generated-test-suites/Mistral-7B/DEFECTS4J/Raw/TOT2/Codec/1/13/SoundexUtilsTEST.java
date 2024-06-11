import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "AbCdEfG";
        String expected = "ABCDEF";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        assertEquals(input, result);

        input = "";
        result = SoundexUtils.clean(input);
        assertEquals(input, result);

        input = "123AbCdEfG";
        result = SoundexUtils.clean(input);
        assertEquals("ABCDEF", result);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Knight";
        String s2 = "Night";
        int expected = 3;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Orange";
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, result);

        s1 = "Šumava";
        s2 = "Šumavá";
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(3, result);
    }

    @Test
    public void testDifferenceEncodedMethod() {
        String es1 = "K234ht";
        String es2 = "N234ht";
        int expected = 3;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "Apple";
        es2 = "Orange";
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);

        es1 = "Šumava";
        es2 = "Šumavá";
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(3, result);
    }
}