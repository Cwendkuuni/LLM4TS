package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.codec.language.SoundexUtils.clean;
import static org.apache.commons.codec.language.SoundexUtils.difference;
import static org.apache.commons.codec.language.SoundexUtils.differenceEncoded;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    /**
     * Test for clean method.
     */
    @Test
    public void testClean() {
        String input = "The quick Brown fox jumps over the lazy dog";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        String actual = clean(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for difference method with Soundex encoder.
     */
    @Test
    public void testDifferenceSoundexEncoder() throws EncoderException {
        String encoderName = "Soundex";
        String encoder = "org.apache.commons.codec.language.Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int actual = difference(new Soundex(), s1, s2);
        Assert.assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        actual = difference(new Soundex(), s1, s2);
        Assert.assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apfel";
        expected = 3;
        actual = difference(new Soundex(), s1, s2);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for differenceEncoded method with Soundex encoder.
     */
    @Test
    public void testDifferenceEncodedSoundexEncoder() {
        String encoderName = "Soundex";
        String encoder = "org.apache.commons.codec.language.Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        String expected = StringUtils.EMPTY;
        String actual = SoundexUtils.differenceEncoded(new Soundex().encode(s1), new Soundex().encode(s2));
        Assert.assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apple";
        expected = "AA23";
        actual = SoundexUtils.differenceEncoded(new Soundex().encode(s1), new Soundex().encode(s2));
        Assert.assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apfel";
        expected = "A230";
        actual = SoundexUtils.differenceEncoded(new Soundex().encode(s1), new Soundex().encode(s2));
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for difference method with RefinedSoundex encoder.
     */
    @Test
    public void testDifferenceRefinedSoundex() throws EncoderException {
        String encoderName = "RefinedSoundex";
        String encoder = "org.apache.commons.codec.language.RefinedSoundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int actual = difference(new RefinedSoundex(), s1, s2);
        Assert.assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        actual = difference(new RefinedSoundex(), s1, s2);
        Assert.assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apfel";
        expected = 3;
        actual = difference(new RefinedSoundex(), s1, s2);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for differenceEncoded method with RefinedSoundex encoder.
     */
    @Test
    public void testDifferenceEncodedRefinedSoundex() {
        String encoderName = "RefinedSoundex";
        String encoder = "org.apache.commons.codec.language.RefinedSoundex";
        String s1 = "Apple";
        String s2 = "Orange";
        String expected = StringUtils.EMPTY;
        String actual = SoundexUtils.differenceEncoded(new RefinedSoundex().encode(s1), new RefinedSoundex().encode(s2));
        Assert.assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apple";
        expected = "AA2323";
        actual = SoundexUtils.differenceEncoded(new RefinedSoundex().encode(s1), new RefinedSoundex().encode(s2));
        Assert.assertEquals(expected, actual);

        s1 = "Apple";
        s2 = "Apfel";
        expected = "A23023";
        actual = SoundexUtils.differenceEncoded(new RefinedSoundex().encode(s1), new RefinedSoundex().encode(s2));
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for difference method with null input.
     */
    @Test(expected = EncoderException.class)
    public void testDifferenceNullEncoder() throws EncoderException {
        String encoderName = null;
        String encoder = "org.apache.commons.codec.language.Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        difference(new Object() {}, encoderName, s1, s2);
    }

    /**
     * Test for difference method with null input1.
     */
    @Test(expected = EncoderException.class)
    public void testDifferenceNullInput1() throws EncoderException {
        String encoderName = "Soundex";
        String encoder = "org.apache.commons.codec.language.Soundex";
        String s1 = null;
        String s2 = "Orange";
        difference(new Soundex(), encoder, s1, s2);
    }

    /**
     * Test for difference method with null input2.
     */
    @Test(expected = EncoderException.class)
    public void testDifferenceNullInput2() throws EncoderException {
        String encoderName = "Soundex";
        String encoder = "org.apache.commons.codec.language.Soundex";
        String s1 = "Apple";
        String s2 = null;
        difference(new Soundex(), encoder, s1, s2);
    }

    /**
     * Test for differenceEncoded method with null input1 encoded String.
     */
    @Test
    public void testDifferenceEncodedNullInput1() {
        String encoderName = "Soundex";
        String encoder = "org.apache.commons.codec.language.Soundex";
        String s1 = null;
        String s2 = "Orange";
        String expected = StringUtils.EMPTY;
        String actual = SoundexUtils.differenceEncoded(new Soundex().encode(s1), new Soundex().encode(s2));
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for differenceEncoded method with null input2 encoded String.
     */
    @Test
    public void testDifferenceEncodedNullInput2() {
        String encoderName = "Soundex";
        String encoder = "org.apache.commons.codec.language.Soundex";
        String s1 = "Apple";
        String s2 = null;
        String expected = StringUtils.EMPTY;
        String actual = SoundexUtils.differenceEncoded(new Soundex().encode(s1), new Soundex().encode(s2));
        Assert.assertEquals(expected, actual);
    }
}