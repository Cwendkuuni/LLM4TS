package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.test.util.Testutils;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for LookupTranslator class
 */
public class LookupTranslatorTest {

    private LookupTranslator translator;
    private StringWriter writer;

    @Before
    public void setUp() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"java", "programming_language"}};
        translator = new LookupTranslator(lookup);
        writer = new StringWriter();
    }

    @Test
    public void testConstructor() {
        // Check if constructor is invoked correctly
        assertEquals("size: 3", 3, translator.lookupMap.size());
        assertEquals("shortest: 5", 5, translator.shortest);
        assertEquals("longest: 6", 6, translator.longest);
    }

    @Test
    public void testTranslateMethodWithValidInput() throws Exception {
        // Test method with valid input
        String input = "HelloWorldjava";
        int expectedLength = 9;
        translator.translate(CharSequence.class.cast(input), 0, writer);
        String result = writer.toString();
        assertEquals("expected: HiEarthprogramming_language", "HiEarthprogramming_language", result);
        assertEquals("input length: " + input.length() + ", written length: " + result.length(), expectedLength, result.length());
    }

    @Test
    public void testTranslateMethodWithEmptyInput() throws Exception {
        // Test method with empty input
        translator.translate(CharSequence.class.cast(""), 0, writer);
        assertEquals("expected: ", "", writer.toString());
    }

    @Test
    public void testTranslateMethodWithInputShorterThanShortestSequence() throws Exception {
        // Test method with input shorter than shortest sequence
        String input = "He";
        translator.translate(CharSequence.class.cast(input), 0, writer);
        assertEquals("expected: ", "", writer.toString());
    }

    @Test
    public void testTranslateMethodWithInputLongerThanLongestSequence() throws Exception {
        // Test method with input longer than longest sequence
        String input = "HelloWorldjavaHelloWorldjava";
        translator.translate(CharSequence.class.cast(input), 0, writer);
        String result = writer.toString();
        assertEquals("expected: HiEarthprogramming_languageHiEarthprogramming_language", "HiEarthprogramming_languageHiEarthprogramming_language", result);
        assertEquals("input length: " + input.length() + ", written length: " + result.length(), 2 * (translator.longest + 1), result.length());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateMethodWithInputLongerThanInputLength() throws Exception {
        // Test method with input longer than input length
        String input = "HelloWorldjava";
        int index = 12;
        translator.translate(CharSequence.class.cast(input), index, writer);
    }

    @Test
    public void testTranslateMethodWithInvalidInput() throws Exception {
        // Test method with invalid input
        String input = null;
        translator.translate(CharSequence.class.cast(input), 0, writer);
        assertNull("expected: null", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateMethodWithNullWriter() throws Exception {
        // Test method with null writer
        translator.translate(CharSequence.class.cast("HelloWorldjava"), 0, null);
    }
}