import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case - input string has mixed case and non-alphabetic characters
        String str = "Thi$ is a T3st!";
        String expected = "THISISATEST";
        Assert.assertEquals(expected, SoundexUtils.clean(str));

        // Edge case - empty input string
        str = "";
        expected = "";
        Assert.assertEquals(expected, SoundexUtils.clean(str));

        // Edge case - input string has no alphabetic characters
        str = "12345";
        expected = "";
        Assert.assertEquals(expected, SoundexUtils.clean(str));
    }

    @Test
    public void testDifference() throws EncoderException {
        // Typical use case - s1 and s2 have similar encoded strings
        StringEncoder encoder = new Soundex();
        String s1 = "Hello";
        String s2 = "Hella";
        int expected = 3;
        Assert.assertEquals(expected, SoundexUtils.difference(encoder, s1, s2));

        // Error scenario - encoder throws EncoderException for one of the strings
        encoder = new Soundex() {
            @Override
            public String encode(String str) throws EncoderException {
                throw new EncoderException("Error encoding string");
            }
        };
        s1 = "Hello";
        s2 = "Hella";
        Assert.assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case - es1 and es2 have similar encoded strings
        String es1 = "H400";
        String es2 = "H404";
        int expected = 3;
        Assert.assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));

        // Edge case - es1 and es2 have no similar characters
        es1 = "ABCD";
        es2 = "EFGH";
        expected = 0;
        Assert.assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));

        // Edge case - es1 and es2 have different lengths
        es1 = "H400";
        es2 = "H40";
        expected = 3;
        Assert.assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
    }

}