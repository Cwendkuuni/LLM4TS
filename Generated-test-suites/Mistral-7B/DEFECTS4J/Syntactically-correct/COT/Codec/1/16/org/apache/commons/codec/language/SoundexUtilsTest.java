package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for SoundexUtils methods.
 */
public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "aBcDefGhIjKlMnOpQrStUvWxYz";
        String expected = "ABCDEFGHIGJKLMNOPQRSTUVWXYZ";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);

        input = null;
        result = SoundexUtils.clean(input);
        Assert.assertNull(result);

        input = "";
        result = SoundexUtils.clean(input);
        Assert.assertEquals("", result);

        input = "   ";
        expected = "";
        result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);

        input = "123";
        result = SoundexUtils.clean(input);
        Assert.assertEquals("", result);

        input = "abcde123efg";
        expected = "ABCDEEFG";
        result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferenceMethod() throws EncoderException {
        StringEncoder encoder = org.apache.commons.codec.encoder.Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);

        s1 = "Kiwi";
        s2 = "Banana";
        expected = 2;
        result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);

        s1 = null;
        s2 = "Apple";
        Assert.assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = null;
        Assert.assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, s2));

        s1 = "";
        s2 = "Apple";
        Assert.assertEquals(0, SoundexUtils.differenceEncoded(SoundexUtils.clean(s1), encoder.encode("Apple")));

        s1 = "Apple";
        s2 = "";
        Assert.assertEquals(0, SoundexUtils.differenceEncoded(encoder.encode("Apple"), SoundexUtils.clean(s2)));
    }

}