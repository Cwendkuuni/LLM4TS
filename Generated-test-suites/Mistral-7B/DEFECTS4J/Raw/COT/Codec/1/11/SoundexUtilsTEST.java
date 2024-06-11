package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "aBcDeF 123";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCleanMethodWithEmptyString() {
        String input = "";
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCleanMethodWithNullString() {
        String input = null;
        String expected = null;
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceMethodWithNullEncoder() {
        String s1 = "apple";
        String s2 = "apples";
        StringEncoder encoder = null;
        try {
            int result = SoundexUtils.difference(encoder, s1, s2);
            fail("Encoder should not be null");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testDifferenceMethodWithNullString1() {
        String s1 = null;
        String s2 = "apple";
        StringEncoder encoder = new StringEncoder() {
            @Override
            public String encode(String string) throws EncoderException {
                return SoundexUtils.clean(string);
            }
        };
        try {
            int result = SoundexUtils.difference(encoder, s1, s2);
            fail("First string should be not null");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDifferenceMethodWithNullString2() {
        String s1 = "apple";
        String s2 = null;
        StringEncoder encoder = new StringEncoder() {
            @Override
            public String encode(String string) throws EncoderException {
                return SoundexUtils.clean(string);
            }
        };
        try {
            int result = SoundexUtils.difference(encoder, s1, s2);
            fail("Second string should be not null");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDifferenceMethod() {
        String s1 = "apple";
        String s2 = "apples";
        StringEncoder encoder = new StringEncoder() {
            @Override
            public String encode(String string) throws EncoderException {
                return SoundexUtils.clean(string);
            }
        };
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(3, result);
    }

    @Test
    public void testDifferenceEncodedMethodWithNullStrings() {
        String es1 = null;
        String es2 = null;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncodedMethodWithEmptyStrings() {
        String es1 = "";
        String es2 = "";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncodedMethod() {
        String es1 = "123A1B2C3D";
        String es2 = "111B1B1C1D";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(4, result);
    }
}