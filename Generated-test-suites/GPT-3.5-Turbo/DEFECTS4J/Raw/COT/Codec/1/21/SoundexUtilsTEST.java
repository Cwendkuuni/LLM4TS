
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = "Test String";
        String expected = "TESTSTRING";
        String actual = SoundexUtils.clean(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new YourEncoder();
        String s1 = "String 1";
        String s2 = "String 2";
        int expected = 2;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "ABCD";
        String es2 = "ABCD";
        int expected = 4;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, actual);
    }

}
