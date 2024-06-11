package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.cli.Utils; // import Util class for testing purpose
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("FOO", SoundexUtils.clean("fOO"));
        assertEquals("BAR", SoundexUtils.clean("bAr"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("THIS IS A TEST", SoundexUtils.clean("tHiS iS a TeSt"));
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNullInput() {
        SoundexUtils.clean(null);
    }

    @Test
    public void testDifferenceEncoderNullInput() throws EncoderException {
        StringEncoder encoder = null;
        String s1 = "foo";
        String s2 = "bar";
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncoderEmptyInput() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "";
        String s2 = "";
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(4, result);
    }

    @Test
    public void testDifferenceEncoderSameInput() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "foo";
        String s2 = "foo";
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(4, result);
    }

    @Test
    public void testDifferenceEncoderDifferentInputs() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "foo";
        String s2 = "bar";
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncoderRefinedSoundex() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.RefinedSoundex();
        String s1 = "foo";
        String s2 = "bar";
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, result);

        String s3 = "foO";
        String s4 = "baR";
        int result2 = SoundexUtils.difference(encoder, s3, s4);
        assertEquals(3, result2);
    }

    @Test
    public void testDifferenceEncodedNullInput() {
        String es1 = null;
        String es2 = "bar";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncodedEmptyInput() {
        String es1 = "";
        String es2 = "";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(4, result);
    }

    @Test
    public void testDifferenceEncodedSameInput() {
        String es1 = "11210";
        String es2 = "11210";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(4, result);
    }

    @Test
    public void testDifferenceEncodedDifferentInputs() {
        String es1 = "11210";
        String es2 = "210";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, result);
    }

    @Test
    public void testDifferenceEncodedRefinedSoundex() {
        String es1 = "11210122";
        String es2 = "21010122";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(3, result);
    }
}