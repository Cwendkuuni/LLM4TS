package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceTranslator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test instance of CharSequenceTranslator.
     */
    private final CharSequenceTranslator translator = new Translator();

    /**
     * Test translate method with empty CharSequence.
     */
    @Test
    public void testEmptyCharSequence() {
        String expected = "";
        String actual = translator.translate(StringUtils.EMPTY);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with single character CharSequence.
     */
    @Test
    public void testSingleCharacter() {
        char input = 'A';
        String expected = String.valueOf(input);
        String actual = translator.translate(String.valueOf(input));
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with multi-character CharSequence.
     */
    @Test
    public void testMultiCharacter() {
        String input = "Hello, World!";
        String expected = "Hello, World!";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with special characters.
     */
    @Test
    public void testSpecialCharacters() {
        String input = "abcd\\ef\\ghij\\kl\\mn\\op\\qrs\\tuv\\wxyz";
        String expected = "abcd\\ef\\ghij\\kl\\mn\\op\\qrs\\tuv\\wxyz";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with surrogate pairs.
     */
    @Test
    public void testSurrogatePairs() {
        String input = "\uD800\uDC00";
        String expected = "\uD800\uDC00";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with IOException.
     */
    @Test(expected = RuntimeException.class)
    public void testIOException() {
        CharSequence input = new String(" Invalid input sequence for use with this translator.");
        translator.translate(input);
    }

    /**
     * Test translate method with null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequence input = "Some input sequence.";
        Writer out = null;
        translator.translate(input, out);
    }

    /**
     * Test translate method with empty Writer.
     */
    @Test
    public void testEmptyWriter() {
        CharSequence input = "Some input sequence.";
        Writer out = new StringWriter(0);
        int consumed = translator.translate(input, 0, out);
        assertEquals(0, consumed);
    }

    /**
     * Test with method with empty array.
     */
    @Test
    public void testWithEmptyArray() {
        CharSequenceTranslator[] translators = {};
        CharSequenceTranslator translator = new Translator().with(translators);
        String input = "Some input sequence.";
        String expected = "Some input sequence.";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test with method with single translator.
     */
    @Test
    public void testWithSingleTranslator() {
        CharSequenceTranslator translator1 = new Translator1();
        CharSequenceTranslator translator = new Translator().with(translator1);
        String input = "Some input sequence.";
        String expected = translator1.translate(input);
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test with method with multiple translators.
     */
    @Test
    public void testWithMultipleTranslators() {
        CharSequenceTranslator translator1 = new Translator1();
        CharSequenceTranslator translator2 = new Translator2();
        CharSequenceTranslator translator = new Translator().with(translator1, translator2);
        String input = "Some input sequence.";
        String expected = translator1.translate(translator2.translate(input));
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }
}