package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "Able was I ere I saw Elba";
        String expected = "ABLE ABLE";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result.toUpperCase());
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.net.URL3Code();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Albert";
        s2 = "Alberto";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Anton";
        s2 = "Antonios";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Alex";
        s2 = "Alfred";
        expected = 2;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Alfa Romeo";
        s2 = "Alpha Romeo";
        expected = 3;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "AE213210";
        String es2 = "A1321010";
        int expected = 3;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }
}