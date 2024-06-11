import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean_NullInput() {
        assertNull("clean(null) should return null", SoundexUtils.clean(null));
    }

    @Test
    public void testClean_EmptyInput() {
        assertEquals("clean(\"\") should return \"\"", "", SoundexUtils.clean(""));
    }

    @Test
    public void testClean_LowerCaseInput() {
        assertEquals("clean(\"abc\") should return \"ABC\"", "ABC", SoundexUtils.clean("abc"));
    }

    @Test
    public void testClean_MixedCaseInput() {
        assertEquals("clean(\"AbC\") should return \"ABC\"", "ABC", SoundexUtils.clean("AbC"));
    }

    @Test
    public void testClean_AllUpperCaseInput() {
        assertEquals("clean(\"ABC\") should return \"ABC\"", "ABC", SoundexUtils.clean("ABC"));
    }

    @Test
    public void testDifference_SoundexEncoder() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "hallo";
        assertEquals("difference(encoder, s1, s2) should return 3", 3, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifference_RefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new RefinedSoundex();
        String s1 = "hello";
        String s2 = "hallo";
        assertEquals("difference(encoder, s1, s2) should return 4", 4, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceEncoded_NullInput() {
        assertEquals("differenceEncoded(null, null) should return 0", 0, SoundexUtils.differenceEncoded(null, null));
    }

    @Test
    public void testDifferenceEncoded_DifferentLengthStrings() {
        String es1 = "abcd";
        String es2 = "abc";
        assertEquals("differenceEncoded(es1, es2) should return 3", 3, SoundexUtils.differenceEncoded(es1, es2));
    }

    @Test
    public void testDifferenceEncoded_SameLengthStrings() {
        String es1 = "abcd";
        String es2 = "abce";
        assertEquals("differenceEncoded(es1, es2) should return 3", 3, SoundexUtils.differenceEncoded(es1, es2));
    }

    @Test
    public void testDifferenceEncoded_SameStrings() {
        String es1 = "abcd";
        String es2 = "abcd";
        assertEquals("differenceEncoded(es1, es2) should return 4", 4, SoundexUtils.differenceEncoded(es1, es2));
    }

}