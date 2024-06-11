import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "aBcDeF123!@#$%";
        String expected = "ABCDEF";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoding() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.soundex.Soundex();
        String s1 = "Apple";
        String s2 = "Apricot";
        int expected = 3;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithInvalidEncoding() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.soundex.Soundex();
        String s1 = "Apple";
        String s2 = null;
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncodedMethod() {
        String es1 = "11214221";
        String es2 = "11210222";
        int expected = 3;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedMethodWithEmptyString() {
        String es1 = "";
        String es2 = "";
        int expected = 4;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

}