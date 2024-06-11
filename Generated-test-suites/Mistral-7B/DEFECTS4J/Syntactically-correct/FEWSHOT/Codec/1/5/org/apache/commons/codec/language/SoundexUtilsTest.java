package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.cli.Utils; // Assuming you have imported Util class for testing clean method
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("HELLO", Utils.stripLeadingAndTrailingQuotes(Utils.clean("HELLO")));
        assertEquals("HELLO", Utils.stripLeadingAndTrailingQuotes(Utils.clean("HELLO")));
        assertEquals("HELLO", Utils.stripLeadingAndTrailingQuotes(Utils.clean("HELLO")));
        assertEquals("HELLO", Utils.stripLeadingAndTrailingQuotes(Utils.clean("HELLO")));
        assertEquals("HELLO", Utils.stripLeadingAndTrailingQuotes(Utils.clean("HELLO")));
        assertEquals("", Utils.stripLeadingAndTrailingQuotes(Utils.clean("")));
        assertEquals("", Utils.stripLeadingAndTrailingQuotes(Utils.clean(" ")));
        assertEquals("ABC", Utils.stripLeadingAndTrailingQuotes(Utils.clean("ABC")));
        assertEquals("ABC", Utils.stripLeadingAndTrailingQuotes(Utils.clean("ABC")));
        assertEquals("ABC", Utils.stripLeadingAndTrailingQuotes(Utils.clean("ABC")));
        assertEquals("ABC", Utils.stripLeadingAndTrailingQuotes(Utils.clean("ABC")));
        assertEquals("ABC", Utils.stripLeadingAndTrailingQuotes(Utils.clean("ABC")));
        assertEquals("", Utils.stripLeadingAndTrailingQuotes(Utils.clean("")));

        // Test null input
        assertNull(Utils.stripLeadingAndTrailingQuotes(null));
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullInputs() {
        SoundexUtils.difference(null, null, null);
    }

    @Test
    public void testDifferenceEncoderNullInput() throws EncoderException {
        StringEncoder encoder = null;
        String s1 = "Soundex";
        String s2 = "Utils";

        assertEquals("Both inputs should not be null", 0, SoundexUtils.difference(encoder, null, s2));
        assertEquals("Second input should not be null", 0, SoundexUtils.difference(encoder, s1, null));
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullStringInput() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = null;
        String s2 = "Utils";

        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoderEmptyInputs() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "";
        String s2 = "";

        assertEquals("Both strings are empty", 4, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncoderThrowsException() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Soundex";
        String s2 = null;

        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceSoundex() {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Soundex";
        String s2 = "Utils";

        assertEquals("Difference between Soundex and Utils", 3, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceRefinedSoundex() {
        StringEncoder encoder = new org.apache.commons.codec.language.RefinedSoundex();
        String s1 = "Soundex";
        String s2 = "Utils";

        assertEquals("Difference between RefinedSoundex and Utils", 5, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceSoundexSameString() {
        StringEncodere encoder = new Soundex();
        String s1 = "Soundex";
        String s2 = "Soundex";

        assertEquals("Difference between same string Soundex", 4, SoundexUtils.difference(encoder, s1, s2));
    }
}