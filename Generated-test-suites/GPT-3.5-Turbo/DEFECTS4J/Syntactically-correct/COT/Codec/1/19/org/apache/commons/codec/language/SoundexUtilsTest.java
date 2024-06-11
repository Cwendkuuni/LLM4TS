import org.junit.Test;
import org.apache.commons.codec.EncoderException;


public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = " Hello World ";
        String expected = "HELLOWORLD";
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testClean_emptyString() {
        String str = "";
        String expected = "";
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testClean_nullString() {
        String str = null;
        String expected = null;
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Hello";
        String s2 = "Halo";
        int expected = 2;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifference_emptyStrings() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "";
        String s2 = "";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_encoderException() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Hello";
        String s2 = "123";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "H450";
        String es2 = "H500";
        int expected = 2;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded_emptyStrings() {
        String es1 = "";
        String es2 = "";
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded_differentLength() {
        String es1 = "H4500";
        String es2 = "H500";
        int expected = 3;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

}