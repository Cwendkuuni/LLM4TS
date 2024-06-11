package org.apache.commons.codec.language;

import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = "A short String";
        String expected = "A SHORT STRING";
        String result = SoundexUtils.clean(str);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNull() {
        String str = null;
        String result = SoundexUtils.clean(str);
        // NullPointerException should be thrown
    }

    @Test
    public void testCleanEmpty() {
        String str = "";
        String expected = "";
        String result = SoundexUtils.clean(str);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceSoundex() {
        StringEncoder encoder = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceSoundexIdentical() {
        StringEncoder encoder = new Soundex();
        String str1 = "Apple";
        String str2 = "Apple";
        int expected = 4;
        int result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceSoundexDifferent() {
        StringEncoder encoder = new Soundex();
        String str1 = "Apple";
        String str2 = "Banana";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceSoundexNull() {
        StringEncoder encoder = null;
        String str1 = "Apple";
        String str2 = "Orange";
        SoundexUtils.difference(encoder, str1, str2);
        // EncoderException should be thrown
    }

    @Test
    public void testDifferenceEncodedSoundex() {
        SoundexUtils soundexUtils = new SoundexUtils();
        String es1 = "1223";
        String es2 = "1213";
        int expected = 2;
        int result = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedSoundexIdentical() {
        SoundexUtils soundexUtils = new SoundexUtils();
        String es1 = "1223";
        String es2 = "1223";
        int expected = 4;
        int result = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedSoundexDifferent() {
        SoundexUtils soundexUtils = new SoundexUtils();
        String es1 = "1223";
        String es2 = "1323";
        int expected = 3;
        int result = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedSoundexNull() {
        SoundexUtils soundexUtils = new SoundexUtils();
        String es1 = null;
        String es2 = "1234";
        int result = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);
    }
}