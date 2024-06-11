package org.junit.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "aBc123DeFgHiJkLmNoPqRsTuVwXyZ";
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String actualOutput = SoundexUtils.clean(input);
        Assert.assertEquals(expectedOutput, actualOutput);

        String emptyString = "";
        String actualOutputEmpty = SoundexUtils.clean(emptyString);
        Assert.assertEquals(emptyString, actualOutputEmpty);

        String nullInput = null;
        String actualOutputNull = SoundexUtils.clean(nullInput);
        Assert.assertNull(actualOutputNull);
    }

    @Test
    public void testDifferenceMethod() {
        String s1 = "Apple";
        String s2 = "Orange";
        int expectedOutput = 0;
        String actualOutput = SoundexUtils.difference(s1, s2);
        Assert.assertEquals(expectedOutput, actualOutput);

        String s3 = "Apple";
        String s4 = "Apple";
        int expectedOutputSame = 4;
        String actualOutputSame = SoundexUtils.difference(s3, s4);
        Assert.assertEquals(expectedOutputSame, actualOutputSame);

        String s5 = "Apple";
        String s6 = "Apple";
        String s6Upper = s6.toUpperCase();
        int expectedOutputUpper = 4;
        String actualOutputUpper = SoundexUtils.difference(s5, s6Upper);
        Assert.assertEquals(expectedOutputUpper, actualOutputUpper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferenceMethodThrowsIllegalArgumentException() {
        String s1 = "Apple";
        String s2 = null;
        int expectedOutput = 0;
        String actualOutput = SoundexUtils.difference(s1, s2);
        // the test should fail since an IllegalArgumentException should be thrown
    }

    @Test
    public void testDifferenceEncodedMethod() {
        String encodedString1 = "122123121";
        String encodedString2 = "121223222";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
        Assert.assertEquals(expectedOutput, actualOutput);

        String encodedString3 = "122123121";
        String encodedString4 = "122123121";
        int expectedOutputSame = 4;
        int actualOutputSame = SoundexUtils.differenceEncoded(encodedString3, encodedString4);
        Assert.assertEquals(expectedOutputSame, actualOutputSame);

        String encodedString5 = "122123121";
        String encodedString6 = "112123121";
        int expectedOutputDifferentLength = 2;
        int actualOutputDifferentLength = SoundexUtils.differenceEncoded(encodedString5, encodedString6);
        Assert.assertEquals(expectedOutputDifferentLength, actualOutputDifferentLength);
    }
}