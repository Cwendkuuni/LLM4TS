package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty input.
     * @throws Exception if tests fail
     */
    @Test
    public void testEmptyInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(CharSequence.EMPTY, writer);
        assertEquals(String.EMPTY, writer.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input.
     * @throws Exception if tests fail
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty writer.
     * @throws Exception if tests fail
     */
    @Test
    public void testEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("test", writer);
        assertNull(writer.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with identity translator.
     * @throws Exception if tests fail
     */
    @Test
    public void testIdentity() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        String expected = "test";
        translator.translate(new StringCharSequence("test"), writer);
        assertEquals(expected, writer.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with custom translator.
     * @throws Exception if tests fail
     */
    @Test
    public void testCustom() throws Exception {
        CharSequenceTranslator translator = new CustomTranslator();
        StringWriter writer = new StringWriter();
        String expected = "XXXXXXXXXXXXXXXXX";
        translator.translate(new StringCharSequence("test"), writer);
        assertEquals(expected, writer.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with merged translators.
     * @throws Exception if tests fail
     */
    @Test
    public void testMergeTranslators() throws Exception {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator custom = new CustomTranslator();
        CharSequenceTranslator merged = identity.with(custom);
        StringWriter writer = new StringWriter();
        String expected = "testXXXXXXXXXXXXXXXXX";
        merged.translate(new StringCharSequence("test"), writer);
        assertEquals(expected, writer.toString());
    }

    /**
     * A simple identity translator that does not change the input.
     */
    static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 1;
            out.write(Character.codePointAt(input, index));
            return consumed;
        }

        @Override
        public String translate(CharSequence input) {
            return input.toString();
        }
    }

    /**
     * A simple custom translator that converts each character into a hexadecimal string.
     */
    static class CustomTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 1;
            out.write(hex(Character.codePointAt(input, index)).getBytes());
            return consumed;
        }
    }

    /**
     * A simple CharSequence implementation that wraps a String.
     */
    static class StringCharSequence implements CharSequence {
        private final String string;

        public StringCharSequence(String string) {
            this.string = string;
        }

        @Override
        public int length() {
            return string.length();
        }

        @Override
        public char charAt(int index) {
            return string.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new StringCharSequence(string.substring(start, end));
        }

        @Override
        public CharSequence toString() {
            return new StringCharSequence(toString());
        }

        @Override
        public String toString() {
            return string;
        }
    }
}