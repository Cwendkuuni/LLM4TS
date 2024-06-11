import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = "Sample123String";
        String expected = "SAMPLESTRING";
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testCleanWithNullString() {
        String expected = null;
        String actual = SoundexUtils.clean(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testCleanWithEmptyString() {
        String expected = "";
        String actual = SoundexUtils.clean("");
        assertEquals(expected, actual);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "SampleString1";
        String s2 = "SampleString2";
        int expected = 3;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceWithNullEncoder() throws EncoderException {
        StringEncoder encoder = null;
        String s1 = "SampleString1";
        String s2 = "SampleString2";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceWithNullStrings() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = null;
        String s2 = null;
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "S123";
        String es2 = "S456";
        int expected = 1;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncodedWithNullStrings() {
        String es1 = null;
        String es2 = null;
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncodedWithDifferentLengths() {
        String es1 = "S123";
        String es2 = "S12";
        int expected = 2;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

}