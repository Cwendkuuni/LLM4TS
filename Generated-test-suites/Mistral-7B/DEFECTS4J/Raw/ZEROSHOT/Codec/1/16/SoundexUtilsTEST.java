import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "aBcDeF 1234!@#$%";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceWithSoundexEncoder() {
        StringEncoder encoder = org.apache.commons.codec.net.URLCodec.INSTANCE;
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "AP53";
        String es2 = "OR31";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "AP53";
        es2 = "AP53";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test(expected = org.apache.commons.codec.EncoderException.class)
    public void testDifferenceWithNullEncoder() {
        String encoder = null;
        String s1 = "Apple";
        String s2 = "Orange";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanWithNullInput() {
        String input = null;
        SoundexUtils.clean(input);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceEncodedWithNullEs1() {
        String es1 = null;
        String es2 = "OR31";
        SoundexUtils.differenceEncoded(es1, es2);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceEncodedWithNullEs2() {
        String es1 = "AP53";
        String es2 = null;
        SoundexUtils.differenceEncoded(es1, es2);
    }
}