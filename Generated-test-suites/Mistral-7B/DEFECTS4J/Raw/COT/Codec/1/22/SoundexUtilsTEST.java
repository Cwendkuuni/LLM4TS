import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "A short String with some Letters123!@#$%^&*()_ Plus Some Digits and Special Characters.";
        String expectedOutput = "A SHORT STRING WITH SOME LETTERS.";
        String actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCleanMethodWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCleanMethodWithNullString() {
        String input = null;
        String expectedOutput = "";
        String actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithNullString1() {
        String encoderType = "Soundex";
        String string1 = null;
        String string2 = "abcdefghijklmnopqrstuvwxyz";
        int expectedOutput = 0;
        try {
            int actualOutput = SoundexUtils.difference(null, string1, string2);
            Assert.assertEquals(expectedOutput, actualOutput);
        } catch (EncoderException e) {
            // Test passes if expected exception is thrown
        }
    }

    @Test
    public void testDifferenceMethodWithNullString2() {
        String encoderType = "Soundex";
        String string1 = "abcdefghijklmnopqrstuvwxyz";
        String string2 = null;
        int expectedOutput = 0;
        try {
            int actualOutput = SoundexUtils.difference(null, string1, string2);
            Assert.assertEquals(expectedOutput, actualOutput);
        } catch (EncoderException e) {
            // Test passes if expected exception is thrown
        }
    }

    @Test
    public void testDifferenceMethodWithNullEncoder() {
        String encoderType = null;
        String string1 = "apple";
        String string2 = "apricot";
        int expectedOutput = 3;
        try {
            int actualOutput = SoundexUtils.difference(encoderType, string1, string2);
            Assert.assertEquals(expectedOutput, actualOutput);
        } catch (EncoderException e) {
            // Test passes if expected exception is thrown
        }
    }

    @Test
    public void testDifferenceMethod() {
        String encoderType = "Soundex";
        String string1 = "apple";
        String string2 = "apricot";
        int expectedOutput = 3;
        try {
            int actualOutput = SoundexUtils.difference(encoderType, string1, string2);
            Assert.assertEquals(expectedOutput, actualOutput);
        } catch (EncoderException e) {
            Assert.fail("Unexpected EncoderException");
        }
    }

    @Test
    public void testDifferenceEncodedMethodWithNullStrings() {
        String encodedString1 = null;
        String encodedString2 = "KJ234";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncodedMethod() {
        String encodedString1 = "P321";
        String encodedString2 = "P622";
        int expectedOutput = 1;
        int actualOutput = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedMethodWithInvalidEncoder() {
        String encoderType = "InvalidEncoder";
        String string1 = "apple";
        String string2 = "apricot";
        SoundexUtils.differenceEncoded(encoderType, string1, string2);
    }

}