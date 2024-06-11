package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;

/**
 * Tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    /**
     * Test translation with empty lookup table.
     */
    @Test
    public void testEmpty() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        StringWriter out = new StringWriter();
        int length = "test".length();
        int translatedLength = translator.translate("test", 0, out) > 0 ? out.toString().length() : 0;
        Assert.assertEquals(0, translatedLength);
    }

    /**
     * Test translation with empty input.
     */
    @Test
    public void testEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "x"}});
        StringWriter out = new StringWriter();
        int length = 0;
        int translatedLength = translator.translate(CharSequence.class.cast(new StringBuilder()), 0, out) > 0 ? out.toString().length() : 0;
        Assert.assertEquals(0, translatedLength);
    }

    /**
     * Test translation with no match.
     */
    @Test
    public void testNoMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "cd"}});
        StringWriter out = new StringWriter();
        int length = "efg".length();
        int translatedLength = translator.translate("efg", 0, out) > 0 ? out.toString().length() : 0;
        Assert.assertEquals(0, translatedLength);
    }

    /**
     * Test translation with a match.
     */
    @Test
    public void testMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "cd"}, {"xyz", "123"}});
        StringWriter out = new StringWriter();
        int length = "xyz".length();
        int translatedLength = translator.translate("xyz", 0, out) > 0 ? out.toString().length() : 0;
        Assert.assertEquals(3, translatedLength);
        Assert.assertEquals("123", out.toString());
    }

    /**
     * Test translation with multiple matches.
     */
    @Test
    public void testMultipleMatches() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "cd"}, {"xyz", "123"}, {"abc", "456"}});
        StringWriter out = new StringWriter();
        int length = "abc".length();
        int translatedLength = translator.translate("abc", 0, out) > 0 ? out.toString().length() : 0;
        Assert.assertEquals(3, translatedLength);
        Assert.assertEquals("456", out.toString());
    }

    /**
     * Test translation with overlapping sequences.
     */
    @Test
    public void testOverlappingSequences() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"abc", "def"}, {"bcd", "ghi"}});
        StringWriter out = new StringWriter();
        int length = "abcdef".length();
        int translatedLength = translator.translate("abcdef", 0, out) > 0 ? out.toString().length() : 0;
        Assert.assertEquals(6, translatedLength);
        Assert.assertEquals("defghi", out.toString());
    }

    /**
     * Test translation with input longer than the longest sequence.
     */
    @Test
    public void testInputLongerThanLongestSequence() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "cd"}});
        StringWriter out = new StringWriter();
        int length = "abcdefghijkl".length();
        int translatedLength = translator.translate("abcdefghijkl", 0, out) > 0 ? out.toString().length() : 0;
        Assert.assertEquals(0, translatedLength);
    }

    /**
     * Test translation with input shorter than the shortest sequence.
     */
    @Test
    public void testInputShorterThanShortestSequence() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"abcd", "efghijklmno"}});
        StringWriter out = new StringWriter();
        int length = "ab".length();
        int translatedLength = translator.translate("ab", 0, out) > 0 ? out.toString().length() : 0;
        Assert.assertEquals(0, translatedLength);
    }

    /**
     * Test translation with null lookup table.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullLookupTable() {
        new LookupTranslator(null);
    }

    /**
     * Test translation with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "cd"}});
        translator.translate(null, 0, new StringWriter());
    }

    /**
     * Test translation with null writer.
     */
    @Test(expected = NullPointerException.class)
    public void testNullWriter() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "cd"}});
        translator.translate("test", 0, null);
    }
}