import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String input1 = "Hello World";
        String expected1 = "HELLOWORLD";
        Assert.assertEquals(expected1, SoundexUtils.clean(input1));

        // Edge case: empty string input
        String input2 = "";
        String expected2 = "";
        Assert.assertEquals(expected2, SoundexUtils.clean(input2));

        // Edge case: null input
        String input3 = null;
        String expected3 = null;
        Assert.assertEquals(expected3, SoundexUtils.clean(input3));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder soundexEncoder = new Soundex();
        StringEncoder refinedEncoder = new RefinedSoundex();

        String s1 = "hello";
        String s2 = "world";

        // Typical use case
        int expected1 = 0;
        Assert.assertEquals(expected1, SoundexUtils.difference(soundexEncoder, s1, s2));

        // Edge case: identical inputs
        s1 = "hello";
        s2 = "hello";
        int expected2 = 4;
        Assert.assertEquals(expected2, SoundexUtils.difference(soundexEncoder, s1, s2));

        // Error scenario: encoder throws exception
        String invalidInput = "123";
        try {
            SoundexUtils.difference(soundexEncoder, invalidInput, s2);
            Assert.fail("EncoderException not thrown");
        } catch (EncoderException e) {
            // Exception caught, test passes
        }
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "HELLO";
        String es2 = "WORLD";

        // Typical use case
        int expected1 = 0;
        Assert.assertEquals(expected1, SoundexUtils.differenceEncoded(es1, es2));

        // Edge case: identical inputs
        es1 = "HELLO";
        es2 = "HELLO";
        int expected2 = 4;
        Assert.assertEquals(expected2, SoundexUtils.differenceEncoded(es1, es2));

        // Edge case: empty string inputs
        es1 = "";
        es2 = "";
        int expected3 = 0;
        Assert.assertEquals(expected3, SoundexUtils.differenceEncoded(es1, es2));

        // Edge case: inputs with different lengths
        es1 = "HELLO";
        es2 = "WORLDHELLO";
        int expected4 = 5;
        Assert.assertEquals(expected4, SoundexUtils.differenceEncoded(es1, es2));
    }

}