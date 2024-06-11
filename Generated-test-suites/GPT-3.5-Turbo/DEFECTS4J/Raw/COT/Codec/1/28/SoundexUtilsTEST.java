
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "Aa2.5^bcD";
        String expected = "AAABCD";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCleanEmptyString() {
        String input = "";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(input, result);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Apache";
        String s2 = "Appache";
        int expected = 4;
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "A1234";
        String es2 = "A1123";
        int expected = 1;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedNullValues() {
        String es1 = null;
        String es2 = "A1123";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);
    }

}
