package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

/**
 * Test cases for SoundexUtils class.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("HELLO", SoundexUtils.clean("HELLO"));
        assertEquals("World", SoundexUtils.clean("World"));
        assertEquals("123", SoundexUtils.clean("123"));
        assertEquals("abcdef", SoundexUtils.clean("abcdef"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanNullInput() {
        SoundexUtils.clean(null);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder soundexEncoder = new org.apache.commons.codec.language.Soundex();
        String str1 = "apple";
        String str2 = "appel";
        int diff = SoundexUtils.difference(soundexEncoder, str1, str2);
        assertEquals(3, diff);

        String str3 = "abc";
        String str4 = "xyz";
        int diff2 = SoundexUtils.difference(soundexEncoder, str3, str4);
        assertEquals(0, diff2);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "A123";
        String es2 = "B456";
        int diff = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, diff);

        String es3 = "A123";
        String es4 = "A123";
        int diff2 = SoundexUtils.differenceEncoded(es3, es4);
        assertEquals(4, diff2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferenceEncodedNullInput() {
        String es1 = null;
        String es2 = "A123";
        SoundexUtils.differenceEncoded(es1, es2);
    }

}