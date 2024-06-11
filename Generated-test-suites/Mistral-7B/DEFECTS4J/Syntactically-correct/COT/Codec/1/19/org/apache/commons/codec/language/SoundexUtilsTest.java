package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

/**
 * Test for SoundexUtils class
 */
public class SoundexUtilsTest {

    /**
     * Test for clean method
     */
    @org.junit.Test
    public void testClean() {
        String input = "aBcDeF123!@#$%^&*()_+";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "";
        result = SoundexUtils.clean(input);
        assertEquals(input, result);

        input = null;
        assertThrows(NullPointerException.class, () -> SoundexUtils.clean(input));
    }

    /**
     * Test for difference method with StringEncoder
     */
    @org.junit.Test
    public void testDifferenceWithStringEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
        String str1 = "Apple";
        String str2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);

        str1 = "Apple";
        str2 = "Apricot";
        expected = 3;
        result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);

        str1 = "Apple";
        str2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);

        String str3 = null;
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(encoder, str1, str2));

        String str4 = "";
        assertThrows(IllegalArgumentException.class, () -> SoundexUtils.difference(encoder, str1, str4));
    }

    /**
     * Test for differenceEncoded method
     */
    @org.junit.Test
    public void testDifferenceEncoded() {
        String es1 = "11230012";
        String es2 = "11010122";
        int expected = 3;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "11230012";
        es2 = "11230012";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = null;
        es2 = "11230012";
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded(es1, es2));

        es1 = "11230012";
        es2 = "";
        assertThrows(IllegalArgumentException.class, () -> SoundexUtils.differenceEncoded(es1, es2));
    }

}