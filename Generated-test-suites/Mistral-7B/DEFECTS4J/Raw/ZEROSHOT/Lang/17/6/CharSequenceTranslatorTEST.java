package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorFactory;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)}.
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "abcdef";
        String expected = "abcdef";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * {@link IOException}.
     */
    @Test
    public void testTranslateIOException() {
        Writer out = new StringWriter();
        CharSequenceTranslator translator = new ThrowingTranslator();
        String input = "abc";
        assertThrows(IOException.class, () -> translator.translate(input, out));
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * {@link IndexOutOfBoundsException}.
     */
    @Test
    public void testTranslateIndexOutOfBounds() {
        CharSequence input = "abcd";
        Writer out = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index == input.length()) {
                    throw new IndexOutOfBoundsException();
                }
                out.write(input.charAt(index));
                return Character.charCount(input.charAt(index));
            }
        };
        assertThrows(IndexOutOfBoundsException.class, () -> translator.translate(input, out));
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testTranslateToString() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "abcdef";
        String expected = "abcdef";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with
     * {@link IOException}.
     */
    @Test
    public void testTranslateToStringIOException() {
        CharSequenceTranslator translator = new ThrowingTranslator();
        String input = "abc";
        assertThrows(RuntimeException.class, () -> translator.translate(input));
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * multiple translators.
     */
    @Test
    public void testTranslateWith() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator upperCase = new UpperCaseTranslator();
        CharSequenceTranslator translator = identity.with(upperCase);
        String input = "abcdef";
        String expected = "ABCDEFabcdef";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link CharSequenceTranslator#hex(int)}.
     */
    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(0x41));
        assertEquals("25", CharSequenceTranslator.hex(0x25));
        assertEquals("42", CharSequenceTranslator.hex(0x42));
    }

    /**
     * Identity translator that does not translate.
     */
    private static class IdentityTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return Character.charCount(input.charAt(index));
        }

    }

    /**
     * Throwing translator that throws {@link IOException}.
     */
    private static class ThrowingTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }

    }

    /**
     * Upper case translator that converts to upper case.
     */
    private static class UpperCaseTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(Character.toUpperCase(input.charAt(index)));
            return Character.charCount(input.charAt(index));
        }

    }

}