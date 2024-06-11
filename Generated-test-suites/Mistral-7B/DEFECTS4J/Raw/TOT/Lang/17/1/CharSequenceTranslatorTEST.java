package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test the translation of a simple CharSequence using the default translator.
     */
    @Test
    public void testDefaultTranslation() {
        CharSequenceTranslator translator = new Translator();
        String input = "Hello, world!";
        String expectedOutput = "Hello, world!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test the translation of a CharSequence containing special characters using the default translator.
     */
    @Test
    public void testSpecialCharacterTranslation() {
        CharSequenceTranslator translator = new Translator();
        String input = "Hello, 世界!";
        String expectedOutput = "Hello, \uD75F\uDC00!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test the translation of a null CharSequence using the default translator.
     */
    @Test(expected = NullPointerException.class)
    public void testNullCharSequence() {
        CharSequenceTranslator translator = new Translator();
        CharSequence input = null;
        translator.translate(input);
    }

    /**
     * Test the translation of an empty CharSequence using the default translator.
     */
    @Test
    public void testEmptyCharSequence() {
        CharSequenceTranslator translator = new Translator();
        String input = "";
        String expectedOutput = "";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test the translation of a CharSequence using a custom translator.
     */
    @Test
    public void testCustomTranslation() {
        CharSequenceTranslator translator = new CustomTranslator();
        String input = "Hello, world!";
        String expectedOutput = "Hi, there!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test the translation of a CharSequence containing special characters using a custom translator.
     */
    @Test
    public void testCustomSpecialCharacterTranslation() {
        CharSequenceTranslator translator = new CustomTranslator();
        String input = "Hello, 世界!";
        String expectedOutput = "Hi, \uD75F\uDC00!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test the translation of a null Writer using the default translator.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new Translator();
        Writer out = null;
        translator.translate(CharSequenceUtils.EMPTY_CHAR_SEQUENCE, 0, out);
    }

    /**
     * Test the translation of a CharSequence using multiple translators.
     */
    @Test
    public void testMultipleTranslators() {
        CharSequenceTranslator defaultTranslator = new Translator();
        CharSequenceTranslator customTranslator = new CustomTranslator();
        CharSequenceTranslator mergedTranslator = defaultTranslator.with(customTranslator);
        String input = "Hello, world!";
        String expectedOutput = "Hi, \uD75F\uDC00, world!";
        String output = mergedTranslator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Default CharSequenceTranslator for testing.
     */
    private static class Translator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            // Implement default translation logic here
        }
    }

    /**
     * Custom CharSequenceTranslator for testing.
     */
    private static class CustomTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            // Implement custom translation logic here
        }
    }
}