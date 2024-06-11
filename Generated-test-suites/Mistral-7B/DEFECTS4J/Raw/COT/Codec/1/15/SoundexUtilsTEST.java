package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SoundexUtils
 */
public class SoundexUtilsTest {

    /**
     * Test method for {@link SoundexUtils#clean(String)}
     */
    @Test
    public void testClean() {
        String input = "aBc DeF gHiJklMnOpQrSt";
        String expected = "ABC DEF GHIJKLMNOPQRST";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}
     */
    @Test
    public void testDifferenceWithEncoder() throws EncoderException {
        StringEncoder encoder = org.apache.commons.codec.net.URLCodec.INSTANCE;
        String s1 = "Apple";
        String s2 = "Orange";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, difference);

        s1 = "Banana";
        s2 = "Apple";
        difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(2, difference);

        s1 = "Apple";
        s2 = "Apple";
        difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(4, difference);
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)}
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "A223210";
        String es2 = "A133110";
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(2, difference);

        es1 = "A000000";
        es2 = "A133110";
        difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, difference);
    }
}