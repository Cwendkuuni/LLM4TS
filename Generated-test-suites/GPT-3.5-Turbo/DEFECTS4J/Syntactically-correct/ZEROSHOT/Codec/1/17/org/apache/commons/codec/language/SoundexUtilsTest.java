import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testCleanWithNullString() {
        String str = null;
        String cleanedStr = SoundexUtils.clean(str);
        Assert.assertNull(cleanedStr);
    }

    @Test
    public void testCleanWithEmptyString() {
        String str = "";
        String cleanedStr = SoundexUtils.clean(str);
        Assert.assertEquals("", cleanedStr);
    }

    @Test
    public void testCleanWithLowerCaseLetters() {
        String str = "abcde";
        String cleanedStr = SoundexUtils.clean(str);
        Assert.assertEquals("ABCDE", cleanedStr);
    }

    @Test
    public void testCleanWithMixedCaseLetters() {
        String str = "abcDe";
        String cleanedStr = SoundexUtils.clean(str);
        Assert.assertEquals("ABCDE", cleanedStr);
    }

    @Test
    public void testDifferenceWithEncoderException() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "string1";
        String s2 = "string2";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(5, difference);
    }

    @Test
    public void testDifferenceWithSameEncodedStrings() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "string";
        String s2 = "string";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(6, difference);
    }

    @Test
    public void testDifferenceEncodedWithNullStrings() {
        String es1 = null;
        String es2 = null;
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, difference);
    }

    @Test
    public void testDifferenceEncodedWithEmptyStrings() {
        String es1 = "";
        String es2 = "";
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, difference);
    }

    @Test
    public void testDifferenceEncodedWithDifferentLengthStrings() {
        String es1 = "abcde";
        String es2 = "abc";
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(3, difference);
    }

    @Test
    public void testDifferenceEncodedWithDifferentCharacters() {
        String es1 = "abcde";
        String es2 = "xyz";
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, difference);
    }
}