import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = "abcDEF123";
        String expected = "ABCDEF";
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testCleanNull() {
        String str = null;
        String expected = null;
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testCleanEmptyString() {
        String str = "";
        String expected = "";
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "abc";
        String s2 = "def";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncoderException() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "abc";
        String s2 = "def";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "ABCDEF";
        String es2 = "DEFABC";
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncodedNullStrings() {
        String es1 = null;
        String es2 = null;
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncodedDifferentLengths() {
        String es1 = "ABC";
        String es2 = "DEFA";
        int expected = 3;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

}