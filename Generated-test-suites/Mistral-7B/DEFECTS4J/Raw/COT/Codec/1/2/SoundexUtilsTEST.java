package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.SoundexUtils.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "aBc123! @ # DEF gHiJklMnopQrST";
        String expected = "ABCDEFGHIJKLMNOPQRST";
        String result = clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCleanEmptyString() {
        String input = "";
        String result = clean(input);
        assertEquals(input, result);
    }

    @Test
    public void testCleanNullString() {
        String input = null;
        String result = clean(input);
        assertEquals(input, result);
    }

    @Test
    public void testDifferenceEncodedNullString() {
        String es1 = null;
        String es2 = "abc";
        int expected = 0;
        int result = differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedNullString2() {
        String es1 = "abc";
        String es2 = null;
        int expected = 0;
        int result = differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedEmptyString() {
        String es1 = "";
        String es2 = "";
        int expected = 4;
        int result = differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedSameString() {
        String es1 = "Soundex1";
        String es2 = "Soundex1";
        int expected = 4;
        int result = differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedException() throws EncoderException {
        String es1 = "abc";
        String es2 = null;
        int result = differenceEncoded(es1, es2);
    }

    @Test
    public void testDifferenceEncodedDifferentStrings() {
        String es1 = "Soundex1";
        String es2 = "Soundex2";
        int expected = 0;
        int result = differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedLongStrings() {
        String es1 = "Soundex123456789012345678901234567890";
        String es2 = "Soundex234567890123456789012345678901234567890";
        int expected = 17;
        int result = differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }
}