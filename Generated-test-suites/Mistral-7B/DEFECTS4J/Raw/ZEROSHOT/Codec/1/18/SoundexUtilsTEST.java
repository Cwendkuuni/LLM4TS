package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    /**
     * Test for clean method.
     */
    @Test
    public void testClean() {
        String input = null;
        String expectedOutput = null;
        String testCaseData[] = { "", "aBc", "123", "AbCdEfG", "ABCdEfG", "ABCdEfGHIJKLMNOPQRSTUVWXYZ",
                "ABCdEfGHIJKLMNOPQRSTUVWXYZ123" };

        for (int i = 0; i < testCaseData.length; i++) {
            input = testCaseData[i];
            expectedOutput = (input.length() > 0) ? input.toUpperCase() : input;
            String output = SoundexUtils.clean(input);
            assertEquals(expectedOutput, output);
        }
    }

    /**
     * Test for difference method.
     */
    @Test
    public void testDifference() throws EncoderException {
        String encoderType = "Soundex";
        String s1 = null;
        String s2 = null;
        String expectedOutput = null;
        int testCaseData[][] = { { null, null }, { null, "test" }, { "test", null },
                { "test", "test" }, { "test1", "test2" }, { "test1", "test1" },
                { "test1", "tesT1" }, { "test1", "test12" }, { "test1", "TEST1" } };

        for (int i = 0; i < testCaseData.length; i++) {
            s1 = testCaseData[i][0];
            s2 = testCaseData[i][1];
            expectedOutput = (s1 != null && s2 != null) ? SoundexUtils.differenceEncoded(SoundexEncoder.INSTANCE.encode(s1),
                    SoundexEncoder.INSTANCE.encode(s2)) : 0;
            int output = SoundexUtils.difference(SoundexEncoder.INSTANCE, s1, s2);
            assertEquals(expectedOutput, output);
        }

        encoderType = "RefinedSoundex";
        testCaseData = new int[][][] { { null, null }, { null, "test" }, { "test", null },
                { "test", "test" }, { "test1", "test2" }, { "test1", "test1" },
                { "test1", "tesT1" }, { "test1", "test12" }, { "test1", "TEST1" },
                { "test1a", "test1b" } };

        for (int i = 0; i < testCaseData.length; i++) {
            s1 = testCaseData[i][0] == null ? null : SoundexUtils.clean(testCaseData[i][0]);
            s2 = testCaseData[i][1] == null ? null : SoundexUtils.clean(testCaseData[i][1]);
            expectedOutput = (s1 != null && s2 != null) ? SoundexUtils.differenceEncoded(SoundexEncoder.INSTANCE.encode(s1),
                    SoundexEncoder.INSTANCE.encode(s2)) : 0;
            int output = SoundexUtils.difference(RefinedSoundexEncoder.INSTANCE, s1, s2);
            assertEquals(expectedOutput, output);
        }
    }

    /**
     * Test for differenceEncoded method.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = null;
        String es2 = null;
        int expectedOutput = 0;
        int testCaseData[][] = { { null, null }, { null, "test" }, { "test", null },
                { "test", "test" }, { "test1", "test2" }, { "test1", "test1" },
                { "test1", "tesT1" }, { "test1", "test12" }, { "test1", "TEST1" },
                { "test1a", "test1b" } };

        for (int i = 0; i < testCaseData.length; i++) {
            es1 = testCaseData[i][0] == null ? null : SoundexUtils.clean(testCaseData[i][0]);
            es2 = testCaseData[i][1] == null ? null : SoundexUtils.clean(testCaseData[i][1]);
            expectedOutput = (es1 != null && es2 != null) ? es1.length() : 0;
            int output = SoundexUtils.differenceEncoded(es1, es2);
            assertEquals(expectedOutput, output);
        }
    }

    /**
     * Test for null pointer exception.
     */
    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        SoundexUtils.difference(null, null, null);
    }

    /**
     * Test for string encoder exception.
     */
    @Test(expected = EncoderException.class)
    public void testEncoderException() throws EncoderException {
        String s1 = "test";
        String encoderType = "Unknown";
        SoundexUtils.difference(EncoderFactory.INSTANCE.getEncoder(encoderType), s1, null);
    }
}