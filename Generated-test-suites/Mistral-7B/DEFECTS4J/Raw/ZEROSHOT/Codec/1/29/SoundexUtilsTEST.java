import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() throws Exception {
        String input = "AéIOú";
        String expected = "AIU";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        assertEquals(input, result);

        input = "";
        result = SoundexUtils.clean(input);
        assertEquals(input, result);
    }

    @Test
    public void testDifference() throws Exception {
        String encoderType = "Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        String encoder = new Soundex();
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Appel";
        expected = 3;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        encoderType = "RefinedSoundex";
        encoder = new org.apache.commons.codec.language.RefinedSoundex();
        s1 = "Apple";
        s2 = "Appel";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Orange";
        expected = 0;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 5;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = null;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertNull(result);

        s2 = null;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertNull(result);

        encoder.reset();
        s1 = "Apple";
        s2 = "Apple";
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        encoder.reset();
        s1 = "Apple";
        s2 = "Apple";
        encoder.encode(s1);
        encoder.encode(s2);
        byte[] encodedS1 = encoder.getOutput();
        byte[] encodedS2 = encoder.getOutput();
        int actual = SoundexUtils.differenceEncoded(new String(encodedS1), new String(encodedS2));
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithException() throws Exception {
        String encoderType = "Soundex";
        String s1 = "Apple";
        String s2 = null;
        String encoder = new Soundex();
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithException2() throws Exception {
        String encoderType = "Soundex";
        String s1 = null;
        String s2 = "Apple";
        String encoder = new Soundex();
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test(expected = DecoderException.class)
    public void testDifferenceWithDecoderException() throws Exception {
        String encoderType = "Soundex";
        String s1 = "Apple";
        String s2 = null;
        String encoder = new Soundex();
        encoder.reset();
        encoder.encode(s1);
        byte[] encodedS1 = encoder.getOutput();
        encoder.flush();
        encoder = null;
        String decoder = new Soundex();
        decoder.reset();
        decoder.decode(encodedS1);
        byte[] decodedS1 = decoder.getOutput();
        decoder.flush();
        decoder = null;
        int result = SoundexUtils.differenceEncoded(new String(encodedS1), null);
    }
}