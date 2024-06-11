package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link CharSequenceTranslator} class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testTranslate() throws Exception {
        // Create translator instance
        CharSequenceTranslator translator = new Translator();

        // Test with simple characters
        String input = "Hello World";
        String expected = "Hello%20World";
        Writer out = new StringWriter();
        int index = 0;
        int codePointsConsumed = translator.translate(CharSequenceUtils.quotedPrintableEncode(input), index, out);
        assertEquals(expected.length(), codePointsConsumed);
        assertEquals(expected, out.toString());

        // Test with empty input
        input = "";
        expected = "";
        out = new StringWriter();
        index = 0;
        codePointsConsumed = translator.translate(input, index, out);
        assertEquals(expected.length(), codePointsConsumed);
        assertEquals(expected, out.toString());

        // Test with multi-byte characters
        input = "Hi 你好";
        expected = "Hi%20%BC%8F%BC%91%BC%80%BC%91";
        out = new StringWriter();
        index = 0;
        codePointsConsumed = translator.translate(input, index, out);
        assertEquals(expected.length(), codePointsConsumed);
        assertEquals(expected, out.toString());

        // Test with exceptions
        input = "Hello\uD800\uDC00World";
        try {
            translator.translate(input, 0, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateWithStringWriter() throws Exception {
        // Create translator instance
        CharSequenceTranslator translator = new Translator();

        // Test with simple characters
        String input = "Hello World";
        String expected = "Hello%20World";
        String result = translator.translate(input);
        assertEquals(expected, result);

        // Test with empty input
        input = "";
        expected = "";
        result = translator.translate(input);
        assertEquals(expected, result);

        // Test with multi-byte characters
        input = "Hi 你好";
        expected = "Hi%20%BC%8F%BC%91%BC%80%BC%91";
        result = translator.translate(input);
        assertEquals(expected, result);

        // Test with surrogate pairs
        input = "Hello\uD800\uDC00World";
        String resultWithSurrogate = translator.translate(input);
        // According to the contract, translators must handle surrogate pairs
        assertEquals(input.length() * 3, resultWithSurrogate.length());

        // Test with exceptions
        try {
            translator.translate(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method.
     */
    @Test
    public void testCompose() throws Exception {
        // Create translators
        CharSequenceTranslator translator1 = new UpperCaseTranslator();
        CharSequenceTranslator translator2 = new LowerCaseTranslator();

        // Compose translators
        CharSequenceTranslator composedTranslator = translator1.with(translator2);

        // Test the composed translator
        String input = "Hello World";
        String expected = "HELLO hello";
        String result = composedTranslator.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with AggregateTranslator.
     */
    @Test
    public void testAggregateTranslator() throws Exception {
        // Create translators
        CharSequenceTranslator translator1 = new Translator();
        CharSequenceTranslator translator2 = new Translator();

        // Compose translators
        CharSequenceTranslator aggregateTranslator = translator1.with(translator2);

        // Test with simple characters
        String input = "Hello World";
        String expected = "Hello%20World%20Hello%20World";
        Writer out = new StringWriter();
        int index = 0;
        int codePointsConsumed = aggregateTranslator.translate(CharSequenceUtils.quotedPrintableEncode(input), index, out);
        assertEquals(expected.length(), codePointsConsumed);
        assertEquals(expected, out.toString());

        // Test with empty input
        input = "";
        expected = "";
        out = new StringWriter();
        index = 0;
        codePointsConsumed = aggregateTranslator.translate(input, index, out);
        assertEquals(expected.length(), codePointsConsumed);
        assertEquals(expected, out.toString());

        // Test with multi-byte characters
        input = "Hi 你好";
        expected = "Hi%20%BC%8F%BC%91%BC%80%BC%91%Hi%20%BC%8F%BC%91%BC%80%BC%91";
        out = new StringWriter();
        index = 0;
        codePointsConsumed = aggregateTranslator.translate(input, index, out);
        assertEquals(expected.length(), codePointsConsumed);
        assertEquals(expected, out.toString());
    }

    /**
     * Test for {@link CharSequenceTranslator#hex(int)} helper method.
     */
    @Test
    public void testHex() {
        // Test with simple characters
        int codepoint = 'A';
        String expected = "41";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);

        // Test with multi-byte characters
        codepoint = 0x4E2D;
        expected = "90%A3";
        result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);
    }

    /**
     * A simple CharSequenceTranslator that does not translate anything.
     */
    static class Translator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }

    /**
     * A CharSequenceTranslator that converts characters to uppercase.
     */
    static class UpperCaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char character = Character.toUpperCase(Character.codePointAt(input, index));
            out.write(character);
            return 1;
        }
    }

    /**
     * A CharSequenceTranslator that converts characters to lowercase.
     */
    static class LowerCaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char character = Character.toLowerCase(Character.codePointAt(input, index));
            out.write(character);
            return 1;
        }
    }

    /**
     * A CharSequenceTranslator that merges multiple translators.
     */
    static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            for (CharSequenceTranslator translator : translators) {
                int translated = translator.translate(input, index, out);
                if (translated > 0) {
                    return translated;
                }
            }
            return 0;
        }
    }
}