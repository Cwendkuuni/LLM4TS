package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

/**
 * Tests for SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test for clean method.
     */
    @Test
    public void testClean() {
        String input = "aBc1Def!@#GhIjKlMnOpQrStUvWxYz";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        String emptyInput = "";
        String emptyExpected = "";
        String resultEmpty = SoundexUtils.clean(emptyInput);
        assertEquals(emptyExpected, resultEmpty);

        String nullInput = null;
        String nullResult = SoundexUtils.clean(nullInput);
        assertEquals(null, nullResult);
    }

    /**
     * Test for difference method with StringEncoder.
     */
    @Test
    public void testDifferenceWithEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String s1 = "apple";
        String s2 = "appel";
        int expected = 3;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        String s3 = "Apple";
        String s4 = "Apple";
        int resultSame = SoundexUtils.difference(encoder, s3, s4);
        assertEquals(4, resultSame);

        String s5 = null;
        String s6 = "Apple";
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s5, s6));

        String s7 = "Apple";
        String s8 = null;
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(encoder, s7, s8));
    }

    /**
     * Test for differenceEncoded method.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "AE11220000";
        String es2 = "AE11220001";
        int expected = 3;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        String es3 = "AE11220000";
        String es4 = "AE11220000";
        int resultSame = SoundexUtils.differenceEncoded(es3, es4);
        assertEquals(4, resultSame);

        String es5 = null;
        String es6 = "AE11220000";
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded(es5, es6));

        String es7 = "AE11220000";
        String es8 = null;
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded(es7, es8));
    }
}