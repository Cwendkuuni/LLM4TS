import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean_NullInput_ReturnNull() {
        String str = null;
        String result = SoundexUtils.clean(str);
        Assert.assertNull(result);
    }

    @Test
    public void testClean_EmptyInput_ReturnEmpty() {
        String str = "";
        String result = SoundexUtils.clean(str);
        Assert.assertEquals("", result);
    }

    @Test
    public void testClean_AllLowerCaseInput_ReturnUpperCase() {
        String str = "abcdefghijklmnopqrstuvwxyz";
        String result = SoundexUtils.clean(str);
        Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", result);
    }

    @Test
    public void testClean_MixedCaseInput_ReturnUpperCase() {
        String str = "AbCdEfGhIjKlMnOpQrStUvWxYz";
        String result = SoundexUtils.clean(str);
        Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", result);
    }

    @Test
    public void testDifference_EqualStrings_ReturnSameLength() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Hello";
        String s2 = "Hello";
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(5, result);
    }

    @Test
    public void testDifference_DifferentStrings_ReturnDifferentLength() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Hello";
        String s2 = "World";
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertNotEquals(5, result);
    }

    @Test
    public void testDifferenceEncoded_EqualStrings_ReturnSameLength() {
        String es1 = "H400";
        String es2 = "H400";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(4, result);
    }

    @Test
    public void testDifferenceEncoded_DifferentStrings_ReturnDifferentLength() {
        String es1 = "H400";
        String es2 = "W400";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertNotEquals(4, result);
    }

}