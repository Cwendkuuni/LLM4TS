import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String input1 = "abc123";
        String expected1 = "ABC";

        // Edge case
        String input2 = "";
        String expected2 = "";

        // Error scenario
        String input3 = null;
        String expected3 = null;

        Assert.assertEquals(expected1, SoundexUtils.clean(input1));
        Assert.assertEquals(expected2, SoundexUtils.clean(input2));
        Assert.assertEquals(expected3, SoundexUtils.clean(input3));
    }

    @Test
    public void testDifference() {
        // Typical use case
        StringEncoderMock encoder = new StringEncoderMock();
        String s1 = "abc";
        String s2 = "abd";
        int expected1 = 2;

        // Edge case
        StringEncoderMock encoder2 = new StringEncoderMock();
        String s3 = "";
        String s4 = "";
        int expected2 = 0;

        // Error scenario
        StringEncoderMock encoder3 = new StringEncoderMock();
        String s5 = null;
        String s6 = "abc";
        int expected3 = 0;

        try {
            Assert.assertEquals(expected1, SoundexUtils.difference(encoder, s1, s2));
            Assert.assertEquals(expected2, SoundexUtils.difference(encoder2, s3, s4));
            Assert.assertEquals(expected3, SoundexUtils.difference(encoder3, s5, s6));
        } catch (EncoderException e) {
            // Handle EncoderException
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case
        String es1 = "ABCD";
        String es2 = "ABCE";
        int expected1 = 3;

        // Edge case
        String es3 = "";
        String es4 = "";
        int expected2 = 0;

        // Error scenario
        String es5 = null;
        String es6 = "ABCD";
        int expected3 = 0;

        Assert.assertEquals(expected1, SoundexUtils.differenceEncoded(es1, es2));
        Assert.assertEquals(expected2, SoundexUtils.differenceEncoded(es3, es4));
        Assert.assertEquals(expected3, SoundexUtils.differenceEncoded(es5, es6));
    }
    
    // Helper class for StringEncoder
    private class StringEncoderMock implements StringEncoder {
        @Override
        public String encode(String str) throws EncoderException {
            if (str == null) {
                return null;
            }
            return str.toUpperCase();
        }
    }
}