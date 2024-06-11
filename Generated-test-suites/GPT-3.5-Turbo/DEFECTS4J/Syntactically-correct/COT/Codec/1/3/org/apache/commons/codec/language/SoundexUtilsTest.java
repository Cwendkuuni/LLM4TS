
import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "  A Bc";
        String expectedOutput = "ABC";
        String actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClean_NullInput() {
        String input = null;
        String expectedOutput = null;
        String actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClean_EmptyInput() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifference() throws EncoderException {
        SoundexEncoder encoder = new SoundexEncoder();
        String s1 = "abc";
        String s2 = "abd";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifference_EncodeError() throws EncoderException {
        String s1 = "abc";
        String s2 = "abd";
        SoundexEncoder encoder = new SoundexEncoder() {
            @Override
            public String encode(String str) throws EncoderException {
                throw new EncoderException("Error encoding string");
            }
        };
        try {
            SoundexUtils.difference(encoder, s1, s2);
            Assert.fail("EncoderException not thrown");
        } catch (EncoderException e) {
            Assert.assertEquals("Error encoding string", e.getMessage());
        }
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "1234";
        String es2 = "1245";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncoded_NullInputs() {
        String es1 = null;
        String es2 = null;
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncoded_DifferentLengths() {
        String es1 = "123";
        String es2 = "12";
        int expectedOutput = 2;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}