package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * JUnit test file for LookupTranslator class.
 */
public class LookupTranslatorTest {

    /**
     * Test constructor with valid lookup table.
     */
    @Test
    public void testLookupTranslatorWithValidLookupTable() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);
    }

    /**
     * Test translate method with valid input and index.
     */
    @Test
    public void testTranslateWithValidInputAndIndex() throws Exception {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "hello world";
        int index = 0;
        int expectedLength = 3 + 5; // "hello" length is 3 and "world" length is 5

        int actualLength = translator.translate(input, index, writer);
        String actualResult = writer.toString();

        Assert.assertEquals(expectedLength, actualLength);
        Assert.assertEquals("hi planet", actualResult);
    }

    /**
     * Test translate method with input shorter than the shortest lookup key.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithInputShorterThanShortestLookupKey() throws Exception {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "h";
        int index = 0;

        translator.translate(input, index, writer);
    }

    /**
     * Test translate method with input longer than the longest lookup key.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithInputLongerThanLongestLookupKey() throws Exception {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "helloworld";
        int index = 0;

        translator.translate(input, index, writer);
    }

    /**
     * Test translate method with input containing no matching lookup key.
     */
    @Test
    public void testTranslateWithInputContainingNoMatchingLookupKey() throws Exception {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "moon";
        int index = 0;

        int actualLength = translator.translate(input, index, writer);
        String actualResult = writer.toString();

        Assert.assertEquals(0, actualLength);
        Assert.assertEquals("", actualResult);
    }

    /**
     * Test translate method with empty lookup table.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][][]{});
    }

    /**
     * Test translate method with null lookup table.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
    }
}