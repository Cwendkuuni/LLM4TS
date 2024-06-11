import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.RefinedSoundex;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = "Thi@s is a Test123!";
        String expected = "THISISATEST";
        String actual = SoundexUtils.clean(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "TestCase";
        String s2 = "TestCase";
        int expected = 4;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "T232";
        String es2 = "T234";
        int expected = 1;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, actual);
    }

}