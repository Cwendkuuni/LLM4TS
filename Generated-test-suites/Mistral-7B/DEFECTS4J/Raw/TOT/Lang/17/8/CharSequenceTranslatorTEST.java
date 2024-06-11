package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method with normal characters.
     */
    @Test
    public void testTranslateNormalCharacters() throws IOException {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "Hello, World!";
        Writer out = new StringWriter();
        int index = 0;
        int expectedConsumed = input.length();
        int actualConsumed = translator.translate(input, index, out);
        assertEquals("Expected to consume all characters", expectedConsumed, actualConsumed);
        assertEquals("Expected translated output to match original input", input, out.toString());
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method with surrogate pairs.
     */
    @Test
    public void testTranslateSurrogatePairs() throws IOException {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "𠮷";
        Writer out = new StringWriter();
        int index = 0;
        int expectedConsumed = 2;
        int actualConsumed = translator.translate(input, index, out);
        assertEquals("Expected to consume two code points", expectedConsumed, actualConsumed);
        assertEquals("Expected translated output to match original input", "\uD840\uDC37", out.toString());
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence)} method with a null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        translator.translate(null);
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence)} method with an empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String output = translator.translate("");
        assertEquals("Expected an empty output for an empty input", "", output);
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence)} method with various special characters.
     */
    @Test
    public void testTranslateSpecialCharacters() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "\u0000\b\f\n\r\t\u007F\"\\/\u007F\u000B\u000E\u000F\u0010\u007F\u0012\u0014\u0016\u0018\u001E\u007F\u001F";
        String output = translator.translate(input).replaceAll("[\\u0000-\\u001F\\u007F]", "");
        assertEquals("Expected all special characters to be removed", "", output);
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with an IOException.
     */
    @Test(expected = RuntimeException.class)
    public void testTranslateIOException() throws IOException {
        CharSequenceTranslator translator = new IOExceptionCharSequenceTranslator();
        String input = "Hello, World!";
        Writer out = new StringWriter();
        int index = 0;
        translator.translate(input, index, out);
    }

    /**
     * Tests the {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method with multiple translators.
     */
    @Test
    public void testWithMultipleTranslators() throws IOException {
        CharSequenceTranslator translator1 = new UppercaseCharSequenceTranslator();
        CharSequenceTranslator translator2 = new LowercaseCharSequenceTranslator();
        CharSequenceTranslator translator = translator1.with(translator2);
        String input = "Hello, WORLD!";
        Writer out = new StringWriter();
        int index = 0;
        translator.translate(input, index, out);
        String output = out.toString();
        assertEquals("Expected the output to be in uppercase followed by lowercase", "HELLO, world!", output);
    }

    /**
     * Test class for an {@link CharSequenceTranslator} that throws an IOException.
     */
    private static class IOExceptionCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("Expected IOException");
        }
    }

    /**
     * Test class for a {@link CharSequenceTranslator} that converts characters to uppercase.
     */
    private static class UppercaseCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.isUpperCase(Character.codePointAt(input, index))
                    ? Character.codePointAt(input, index)
                    : Character.toUpperCase(Character.codePointAt(input, index));
            out.write(c);
            return Character.charCount(c);
        }
    }

    /**
     * Test class for a {@link CharSequenceTranslator} that converts characters to lowercase.
     */
    private static class LowercaseCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.isUpperCase(Character.codePointAt(input, index))
                    ? (char) (Character.codePointAt(input, index) + Character.MIN_VALUE)
                    : Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }
    }
}