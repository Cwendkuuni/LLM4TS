import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Test case 1: input string is null
        String input1 = null;
        String expected1 = null;
        Assert.assertEquals(expected1, SoundexUtils.clean(input1));

        // Test case 2: input string is empty
        String input2 = "";
        String expected2 = "";
        Assert.assertEquals(expected2, SoundexUtils.clean(input2));

        // Test case 3: input string contains special characters and numbers
        String input3 = "abc123@!#";
        String expected3 = "ABC";
        Assert.assertEquals(expected3, SoundexUtils.clean(input3));

        // Test case 4: input string contains upper case letters
        String input4 = "aBcDeF";
        String expected4 = "ABCDEF";
        Assert.assertEquals(expected4, SoundexUtils.clean(input4));

        // Test case 5: input string is already clean
        String input5 = "ABCDEF";
        String expected5 = "ABCDEF";
        Assert.assertEquals(expected5, SoundexUtils.clean(input5));
    }

    @Test
    public void testDifference() throws EncoderException {
        // Test case 1: s1 and s2 are both null
        StringEncoder encoder1 = null;
        String s1 = null;
        String s2 = null;
        int expected1 = 0;
        Assert.assertEquals(expected1, SoundexUtils.difference(encoder1, s1, s2));

        // Test case 2: s1 and s2 are both empty
        StringEncoder encoder2 = null;
        String s3 = "";
        String s4 = "";
        int expected2 = 0;
        Assert.assertEquals(expected2, SoundexUtils.difference(encoder2, s3, s4));

        // Test case 3: s1 and s2 are different by 1 character
        StringEncoder encoder3 = null;
        String s5 = "abcd";
        String s6 = "abce";
        int expected3 = 3;
        Assert.assertEquals(expected3, SoundexUtils.difference(encoder3, s5, s6));

        // Test case 4: s1 and s2 are the same
        StringEncoder encoder4 = null;
        String s7 = "abc";
        String s8 = "abc";
        int expected4 = 3;
        Assert.assertEquals(expected4, SoundexUtils.difference(encoder4, s7, s8));

    }

    @Test
    public void testDifferenceEncoded() {
        // Test case 1: es1 and es2 are both null
        String es1 = null;
        String es2 = null;
        int expected1 = 0;
        Assert.assertEquals(expected1, SoundexUtils.differenceEncoded(es1, es2));

        // Test case 2: es1 and es2 are both empty
        String es3 = "";
        String es4 = "";
        int expected2 = 0;
        Assert.assertEquals(expected2, SoundexUtils.differenceEncoded(es3, es4));

        // Test case 3: es1 and es2 are different by 1 character
        String es5 = "abcd";
        String es6 = "abce";
        int expected3 = 3;
        Assert.assertEquals(expected3, SoundexUtils.differenceEncoded(es5, es6));

        // Test case 4: es1 and es2 are the same
        String es7 = "abc";
        String es8 = "abc";
        int expected4 = 3;
        Assert.assertEquals(expected4, SoundexUtils.differenceEncoded(es7, es8));
    }
}