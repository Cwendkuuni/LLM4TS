import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert;

public class SoundexUtilsTest {

    @Test
    public void testClean_NullInput_ReturnsNull() {
        String str = null;
        String result = SoundexUtils.clean(str);
        Assert.assertNull(result);
    }

    @Test
    public void testClean_EmptyInput_ReturnsEmptyString() {
        String str = "";
        String result = SoundexUtils.clean(str);
        Assert.assertEquals("", result);
    }

    @Test
    public void testClean_OnlyLowercaseLetters_ReturnsUppercase() {
        String str = "abc";
        String result = SoundexUtils.clean(str);
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testClean_MixedLettersAndDigits_ReturnsOnlyLettersInUppercase() {
        String str = "a1b2c3";
        String result = SoundexUtils.clean(str);
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testDifference_NullEncoder_ReturnsZero() throws EncoderException {
        StringEncoder encoder = null;
        String s1 = "abc";
        String s2 = "def";
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testDifference_NullInputStrings_ReturnsZero() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = null;
        String s2 = null;
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testDifference_EncodedStringsAreDifferent_ReturnsZero() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "abc";
        String s2 = "def";
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testDifference_EncodedStringsAreSame_ReturnsCorrectValue() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "abc";
        String s2 = "abd";
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(3, result);
    }

    @Test
    public void testDifferenceEncoded_NullInputStrings_ReturnsZero() {
        String es1 = null;
        String es2 = null;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncoded_EncodedStringsAreDifferent_ReturnsZero() {
        String es1 = "123";
        String es2 = "456";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncoded_EncodedStringsAreSame_ReturnsCorrectValue() {
        String es1 = "123";
        String es2 = "133";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(2, result);
    }

}