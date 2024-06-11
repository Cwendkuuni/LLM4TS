import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "AAn Apple a day keeps the Doctor away";
        String expected = "A A A A A A A A A A A";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifference() throws EncoderException {
        String encoderType = "Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(expected, result);

        encoderType = "RefinedSoundex";
        s1 = "Apple";
        s2 = "Apple";
        expected = 5;
        result = SoundexUtils.difference(new RefinedSoundex(), s1, s2);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNullEncoder() throws EncoderException {
        String s1 = "Apple";
        String s2 = "Apple";
        SoundexUtils.difference(null, s1, s2);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceWithNullString() {
        String encoderType = "Soundex";
        StringEncoder encoder = new Soundex();
        int result = SoundexUtils.difference(encoder, null, "Apple");
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceWithNullString2() {
        String encoderType = "Soundex";
        StringEncoder encoder = new Soundex();
        int result = SoundexUtils.difference(encoder, "Apple", null);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "AA123";
        String es2 = "AA123";
        int expected = 5;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedDifferentLength() {
        String es1 = "AA123";
        String es2 = "AA1";
        int expected = 1;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }
}