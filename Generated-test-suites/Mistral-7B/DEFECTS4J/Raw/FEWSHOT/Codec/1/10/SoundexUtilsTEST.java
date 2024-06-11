package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

/**
 * Test cases for {@link SoundexUtils} class.
 */
public class SoundexUtilsTest {

    /**
     * Test for {@link SoundexUtils#clean(String)} method.
     */
    @Test
    public void testClean() {
        String input = "The quick brown fox jumps over the lazy dog.";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG.";
        String result = SoundexUtils.clean(input);
        assertTrue(result.equals(expected));
    }

    /**
     * Test for {@link SoundexUtils#clean(String)} method with null input.
     */
    @Test
    public void testCleanNullInput() {
        String input = null;
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link SoundexUtils#clean(String)} method with empty string input.
     */
    @Test
    public void testCleanEmptyInput() {
        String input = "";
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)} method.
     */
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.SoundexEncoder();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Smith";
        s2 = "Smithe";
        expected = 3;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Schmidt";
        s2 = "Schmidtt";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "K213";
        es2 = "K213";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "A123";
        es2 = "A124";
        expected = 1;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method with null input.
     */
    @Test
    public void testDifferenceEncodedNullInput() {
        String es1 = null;
        String es2 = "";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }
}