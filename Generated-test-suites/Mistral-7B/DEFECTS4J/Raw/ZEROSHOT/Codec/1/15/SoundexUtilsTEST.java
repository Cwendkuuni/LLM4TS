import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "The quick Brown fox jumps over the lazy dog";
        String expected = "The quick Brown Fox Jumps Over The Lazy Dog";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        assertEquals(input, result);

        input = "";
        result = SoundexUtils.clean(input);
        assertEquals(input, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNullInputThrowsException() {
        SoundexUtils.clean(null);
    }

    @Test
    public void testDifferenceEncoded() {
        String encoder = new Soundex().getName();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.differenceEncoded(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Orangutan";
        expected = 2;
        result = SoundexUtils.differenceEncoded(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple sauce";
        expected = 3;
        result = SoundexUtils.differenceEncoded(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedWithInvalidEncoderThrowsException() {
        SoundexUtils.differenceEncoded(null, "Apple", "Orange");
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceEncodedNullStringThrowsException() {
        String encoder = new Soundex().getName();
        SoundexUtils.differenceEncoded(encoder, null, "Orange");
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceEncodedNullString2ThrowsException() {
        String encoder = new Soundex().getName();
        SoundexUtils.differenceEncoded(encoder, "Apple", null);
    }

    @Test
    public void testDifference() throws EncoderException {
        String encoder = new Soundex().getName();
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

        s1 = "Apple";
        s2 = "Orangutan";
        expected = 2;
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple sauce";
        expected = 3;
        result = SoundexUtils.difference(new Soundex(), s1, s2);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullEncoderThrowsException() {
        SoundexUtils.difference(null, "Apple", "Orange");
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullStringThrowsException() throws EncoderException {
        String encoder = new Soundex().getName();
        SoundexUtils.difference(encoder, null, "Orange");
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullString2ThrowsException() throws EncoderException {
        String encoder = new Soundex().getName();
        SoundexUtils.difference(encoder, "Apple", null);
    }
}