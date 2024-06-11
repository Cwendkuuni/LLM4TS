import static org.junit.Assert.assertEquals;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = "abc123";
        String expected = "ABC";
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testClean_NullString() {
        String str = null;
        String expected = null;
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testClean_EmptyString() {
        String str = "";
        String expected = "";
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "hello";
        int expected = 4;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_EncodeError() throws EncoderException {
        StringEncoder encoder = new Soundex() {
            @Override
            public String encode(String str) throws EncoderException {
                throw new EncoderException("Encode error");
            }
        };
        String s1 = "hello";
        String s2 = "hello";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "H400";
        String es2 = "H400";
        int expected = 4;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded_DifferentLength() {
        String es1 = "H40";
        String es2 = "H400";
        int expected = 2;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded_NullString() {
        String es1 = null;
        String es2 = null;
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded_FirstEmpty() {
        String es1 = "";
        String es2 = "H400";
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

}