package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.SoundexUtils.clean;
import static org.apache.commons.codec.language.SoundexUtils.difference;
import static org.apache.commons.codec.language.SoundexUtils.differenceEncoded;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "The quick Brown fox jumps over the lazy dog.";
        String expected = "The quick Brown Fox jumps over the lazy Dog.";
        String actual = SoundexUtils.clean(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceNullStrings() {
        String es1 = null;
        String es2 = null;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, actual);
    }

    @Test
    public void testDifferenceEmptyStrings() {
        String es1 = "";
        String es2 = "";
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, actual);
    }

    @Test
    public void testDifferenceNullES1() {
        String es2 = "test";;
        int actual = SoundexUtils.differenceEncoded(null, es2);
        assertEquals(0, actual);
    }

    @Test
    public void testDifferenceNullES2() {
        String es1 = "test";
        int actual = SoundexUtils.differenceEncoded(es1, null);
        assertEquals(0, actual);
    }

    @Test
    public void testDifferenceEmptyES1() {
        String es1 = "";
        String es2 = "test";
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, actual);
    }

    @Test
    public void testDifferenceES1ShorterThanES2() {
        String es1 = "test";
        String es2 = "testing";
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(2, actual);
    }

    @Test
    public void testDifferenceES1LongerThanES2() {
        String es1 = "testing";
        String es2 = "test";
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(2, actual);
    }

    @Test
    public void testDifferenceSameStrings() {
        String es1 = "test";
        String es2 = "test";
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(4, actual);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsEncoderException() {
        String es1 = "test";
        String es2 = null;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
    }

}