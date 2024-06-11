package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.cli.Utils; // For testing purposes, importing the package containing the Util class
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Testing with null and empty strings
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));

        // Testing with simple strings
        assertEquals("HELLO", SoundexUtils.clean("HELLO"));
        assertEquals("HELLO", SoundexUtils.clean("Hello"));
        assertEquals("HELLO", SoundexUtils.clean(" Hel lo "));
        assertEquals("HELLO", SoundexUtils.clean("HELLO"));

        // Testing with strings that contain digits and special characters
        assertEquals("HELLO", SoundexUtils.clean("HELLO123"));
        assertEquals("HELLO", SoundexUtils.clean("HELLO!@#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`"));

        // Testing with long strings
        String longString = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        assertEquals(longString.toUpperCase(), SoundexUtils.clean(longString));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.soundex.SoundexEncoder();
        String s1 = "Apple";
        String s2 = "Orange";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, difference);

        s1 = "Apple";
        s2 = "Orange";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(0, difference);

        s1 = "Apple";
        s2 = "Apple";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(4, difference);

        s1 = "Apple";
        s2 = "ApplE";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(3, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(2, difference);

        s1 = "Apple";
        s2 = "April";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(2, difference);

        s1 = "Apple";
        s2 = "April";
        difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(2, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(2, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(2, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.differenceEncoded(encoder.encode("Apple"), encoder.encode("Apricot"));
        assertEquals(2, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.differenceEncoded(encoder.encode("Apple"), encoder.encode("Apricot"));
        assertEquals(2, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.differenceEncoded(encoder.encode("Apple"), encoder.encode("Apricot"));
        assertEquals(2, difference);
    }

    @Test
    public void testDifferenceWithRefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.refinedsoundex.RefinedSoundexEncoder();
        String s1 = "Apple";
        String s2 = "Orange";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, difference);

        s1 = "Apple";
        s2 = "Orange";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(0, difference);

        s1 = "Apple";
        s2 = "Apple";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(4, difference);

        s1 = "Apple";
        s2 = "Apple";
        difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(4, difference);

        s1 = "Apple";
        s2 = "ApplE";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(3, difference);

        s1 = "Apple";
        s2 = "April";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(3, difference);

        s1 = "Apple";
        s2 = "April";
        difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(3, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
        assertEquals(3, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(3, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.differenceEncoded(encoder.encode("Apple"), encoder.encode("Apricot"));
        assertEquals(3, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.differenceEncoded(encoder.encode("Apple"), encoder.encode("Apricot"));
        assertEquals(3, difference);

        s1 = "Apple";
        s2 = "Apricot";
        difference = SoundexUtils.differenceEncoded(encoder.encode("Apple"), encoder.encode("Apricot"));
        assertEquals(3, difference);
    }
}