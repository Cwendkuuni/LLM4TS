package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String expected = "";
        String actual = translator.translate(CharSequenceUtils.EMPTY_STRING);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with single character input.
     */
    @Test
    public void testTranslateSingleCharacterInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "A";
        String expected = "A";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with multiple characters input.
     */
    @Test
    public void testTranslateMultipleCharactersInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello World!";
        String expected = "Hello World!";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with surrogate pairs input.
     */
    @Test
    public void testTranslateSurrogatePairsInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "👋";
        String expected = "👋";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with error input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = null;
        translator.translate(input);
    }

    /**
     * Test translate method with null Writer output.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriterOutput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer out = null;
        int index = 0;
        int length = 0;
        CharSequence input = "Test";
        int consumed = translator.translate(input, index, out);
    }

    /**
     * Test translate method with IOException.
     */
    @Test(expected = IOException.class)
    public void testTranslateIOException() throws IOException {
        StringWriter out = new StringWriter();
        CharSequenceTranslator translator = new IdentityTranslatorWithIOException();
        CharSequence input = "Test";
        int index = 0;
        int length = 0;
        int consumed = translator.translate(input, index, out);
    }

    /**
     * Test translate method with multiple translators.
     */
    @Test
    public void testTranslateMultipleTranslators() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator uppercaseTranslator = new UppercaseTranslator();
        CharSequenceTranslator translator = identityTranslator.with(uppercaseTranslator);
        String input = "Hello World!";
        String expected = "HELLO WORLD!";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Identity translator that does not modify the input.
     */
    public static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int len = Character.codePointCount(input, index);
            out.write(Character.toChars(Character.codePointAt(input, index)), 0, len);
            return len;
        }
    }

    /**
     * Identity translator that throws IOException intentionally.
     */
    public static class IdentityTranslatorWithIOException extends IdentityTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("Test IOException");
        }
    }

    /**
     * Translator that translates characters to uppercase.
     */
    public static class UppercaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int len = Character.codePointCount(input, index);
            out.write(Character.toChars(Character.toUpperCase(Character.codePointAt(input, index))));
            return len;
        }
    }

    /**
     * Aggregate translator merging this translator with other translators.
     */
    public static class AggregateTranslator extends CharSequenceTranslator {
        private CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int totalConsumed = 0;
            for (CharSequenceTranslator translator : translators) {
                totalConsumed += translator.translate(input, index, out);
                index += totalConsumed;
            }
            return totalConsumed;
        }
    }
}