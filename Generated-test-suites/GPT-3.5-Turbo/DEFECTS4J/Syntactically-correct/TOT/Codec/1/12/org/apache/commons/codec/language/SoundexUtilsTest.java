import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;

public class SoundexUtilsTest {

    private SoundexUtils soundexUtils;

    @Before
    public void setUp() {
        soundexUtils = new SoundexUtils();
    }

    // Test cases for clean() method

    @Test
    public void testCleanWithNullString() {
        String inputString = null;
        String expected = null;
        String actual = soundexUtils.clean(inputString);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCleanWithEmptyString() {
        String inputString = "";
        String expected = "";
        String actual = soundexUtils.clean(inputString);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCleanWithLowercaseLetters() {
        String inputString = "abcde";
        String expected = "ABCDE";
        String actual = soundexUtils.clean(inputString);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCleanWithUppercaseLetters() {
        String inputString = "ABCDE";
        String expected = "ABCDE";
        String actual = soundexUtils.clean(inputString);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCleanWithMixedCaseLetters() {
        String inputString = "AbCdE";
        String expected = "ABCDE";
        String actual = soundexUtils.clean(inputString);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCleanWithNonLetterCharacters() {
        String inputString = "ABC123!@#";
        String expected = "ABC";
        String actual = soundexUtils.clean(inputString);
        Assert.assertEquals(expected, actual);
    }

    // Test cases for difference() method

    @Test
    public void testDifferenceWithNullEncoder() throws EncoderException {
        StringEncoder encoder = null;
        String s1 = "ABC";
        String s2 = "DEF";
        int expected = 0;
        int actual = soundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceWithNullStrings() throws EncoderException {
        StringEncoder encoder = new SoundexEncoder();
        String s1 = null;
        String s2 = null;
        int expected = 0;
        int actual = soundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceWithEmptyStrings() throws EncoderException {
        StringEncoder encoder = new SoundexEncoder();
        String s1 = "";
        String s2 = "";
        int expected = 0;
        int actual = soundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceWithSameEncodedStrings() throws EncoderException {
        StringEncoder encoder = new SoundexEncoder();
        String s1 = "ABC";
        String s2 = "ABC";
        int expected = 4;
        int actual = soundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceWithDifferentEncodedStrings() throws EncoderException {
        StringEncoder encoder = new SoundexEncoder();
        String s1 = "ABC";
        String s2 = "DEF";
        int expected = 1;
        int actual = soundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
    }

    // Test cases for differenceEncoded() method

    @Test
    public void testDifferenceEncodedWithNullStrings() {
        String es1 = null;
        String es2 = null;
        int expected = 0;
        int actual = soundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncodedWithEmptyStrings() {
        String es1 = "";
        String es2 = "";
        int expected = 0;
        int actual = soundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncodedWithSameStrings() {
        String es1 = "ABC";
        String es2 = "ABC";
        int expected = 3;
        int actual = soundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncodedWithDifferentStrings() {
        String es1 = "ABC";
        String es2 = "DEF";
        int expected = 0;
        int actual = soundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, actual);
    }

}