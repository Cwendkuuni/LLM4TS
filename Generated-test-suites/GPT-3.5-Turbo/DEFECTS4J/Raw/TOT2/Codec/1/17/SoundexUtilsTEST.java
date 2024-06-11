package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "abc123";
        String expected = "ABC";
        assertEquals(expected, SoundexUtils.clean(input));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new RefinedSoundex();
        String s1 = "hello";
        String s2 = "halo";
        int expected = 3;
        assertEquals(expected, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "123";
        String es2 = "1234";
        int expected = 3;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
    }

    @Test
    public void testCleanNull() {
        String input = null;
        assertNull(SoundexUtils.clean(input));
    }

    @Test
    public void testDifferenceNull() {
        StringEncoder encoder = new RefinedSoundex();
        String s1 = null;
        String s2 = "hello";
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceEncodedNull() {
        String es1 = null;
        String es2 = "123";
        int expected = 0;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
    }

}