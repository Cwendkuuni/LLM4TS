package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    /**
     * Test for empty input.
     */
    @Test
    public void testEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String result = translator.translate(CharSequence.EMPTY);
        assertEquals("", result);
    }

    /**
     * Test translation of a single character.
     */
    @Test
    public void testSingleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String result = translator.translate(CharSequence.of("A"));
        assertEquals("A", result);
    }

    /**
     * Test translation of a multi-character sequence.
     */
    @Test
    public void testMultiCharacterSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String result = translator.translate(CharSequence.of("ABC"));
        assertEquals("ABC", result);
    }

    /**
     * Test translation with Writer.
     */
    @Test
    public void testWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(CharSequence.of("ABC"), writer);
        String result = writer.toString();
        assertEquals("ABC", result);
    }

    /**
     * Test translation with null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(CharSequence.of("ABC"), null);
    }

    /**
     * Test translation merging multiple translators.
     */
    @Test
    public void testMergingTranslators() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();

        CharSequenceTranslator merged = identity.with(hex);
        StringWriter writer = new StringWriter();
        merged.translate(CharSequence.of("A1B2C3"), writer);
        String result = writer.toString();
        assertArrayEquals(new String[]{"A", "1", "B", "2", "C", "3"}, result.split(""));
    }

    /**
     * Identity translator that just returns the input as is.
     */
    static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isSurrogate(c)) {
                char high = Character.highSurrogate(c);
                char low = Character.lowSurrogate(c);
                out.write(Character.toChars(Character.toCodePoint(high, low[0])));
                index++;
                return Character.charCount(c);
            }
            out.write(Character.toChars(c));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            try {
                StringWriter writer = new StringWriter(input.length() * 2);
                translate(input, writer);
                return writer.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Hex translator that converts each character to its upper case hexadecimal representation.
     */
    static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isSurrogate(c)) {
                char high = Character.highSurrogate(c);
                char low = Character.lowSurrogate(c);
                out.write(CharSequence.of(hex(Character.codePointAt(input, index++)), hex(Character.codePointAt(input, index))).toArray(new char[0]));
                index++;
                return Character.charCount(c);
            }
            out.write(CharSequence.of(hex(c)).toArray(new char[0]));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                translate(input, new StringWriter(os));
                return new String(os.toByteArray(), "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}