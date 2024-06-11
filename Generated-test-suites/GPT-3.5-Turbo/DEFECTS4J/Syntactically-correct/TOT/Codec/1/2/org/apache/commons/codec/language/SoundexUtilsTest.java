import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean_NullString_ReturnsNull() {
        String str = null;
        String cleanedStr = SoundexUtils.clean(str);
        assertNull(cleanedStr);
    }

    @Test
    public void testClean_EmptyString_ReturnsEmptyString() {
        String str = "";
        String cleanedStr = SoundexUtils.clean(str);
        assertEquals("", cleanedStr);
    }

    @Test
    public void testClean_OnlyUpperCase_ReturnsSameString() {
        String str = "HELLO";
        String cleanedStr = SoundexUtils.clean(str);
        assertEquals("HELLO", cleanedStr);
    }

    @Test
    public void testClean_MixedCase_ReturnsUpperCaseString() {
        String str = "HelloWorld";
        String cleanedStr = SoundexUtils.clean(str);
        assertEquals("HELLOWORLD", cleanedStr);
    }

    @Test
    public void testDifference_SameEncodedStrings_ReturnsStringLength() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "hello";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(s1.length(), difference);
    }

    @Test
    public void testDifference_DifferentEncodedStrings_ReturnsZero() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "world";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_EncodingError_ThrowsEncoderException() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = null;
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoded_SameStrings_ReturnsStringLength() {
        String es1 = "1234";
        String es2 = "1234";
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(es1.length(), difference);
    }

    @Test
    public void testDifferenceEncoded_DifferentStrings_ReturnsZero() {
        String es1 = "1234";
        String es2 = "5678";
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, difference);
    }

    @Test
    public void testDifferenceEncoded_NullString_ReturnsZero() {
        String es1 = "1234";
        String es2 = null;
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, difference);
    }

    @Test
    public void testDifferenceEncoded_EmptyString_ReturnsZero() {
        String es1 = "1234";
        String es2 = "";
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, difference);
    }
}